package com.mvi.common

import com.arkivanov.mvikotlin.core.annotations.MainThread
import com.arkivanov.mvikotlin.core.store.Bootstrapper
import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.utils.internal.atomic
import com.arkivanov.mvikotlin.utils.internal.initialize
import com.arkivanov.mvikotlin.utils.internal.requireValue
import com.common.coroutines.DispatchersProvider
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

typealias SuspendIntentExecutor<Intent, State, Result, Label> =
    SuspendDelegationExecutor<Intent, Nothing, State, Result, Label>

typealias SuspendActionExecutor<Action, State, Result, Label> =
    SuspendDelegationExecutor<Nothing, Action, State, Result, Label>

open class SuspendDelegationExecutor<in Intent : Any, in Action : Any, State : Any, Result : Any, Label : Any>(
    private val dispatchersProvider: DispatchersProvider,
    private val intentDelegates: Set<IntentExecutorDelegate<Intent, State, Result, Label>> = emptySet(),
    private val actionDelegates: Set<ActionExecutorDelegate<Action, State, Result, Label>> = emptySet()
) : Executor<Intent, Action, State, Result, Label> {

    private val callbacks = atomic<Executor.Callbacks<State, Result, Label>>()
    val state: () -> State = { callbacks.requireValue().state }
    private val errorHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, e ->
        onErrorOccur(e)
    }
    val scope = CoroutineScope(
        SupervisorJob() + dispatchersProvider.unconfined + errorHandler
    )

    final override fun init(callbacks: Executor.Callbacks<State, Result, Label>) {
        this.callbacks.initialize(callbacks)
    }

    final override fun handleIntent(intent: Intent) {
        val executor = this
        with(scope) {
            launch {
                executeIntent(intent, state)
            }
            intentDelegates.forEach {
                launch {
                    it.executeIntent(
                        intent,
                        executor
                    )
                }
            }
        }
    }

    /**
     * A suspending variant of the [Executor.handleIntent] method.
     * The coroutine is launched in a scope which closes when the [Executor] is disposed.
     *
     * @param intent an `Intent` received by the [Store]
     * @param getState a `State` supplier that returns the *current* `State` of the [Store]
     */
    @MainThread
    protected open suspend fun executeIntent(intent: Intent, getState: () -> State) {
    }

    final override fun handleAction(action: Action) {
        val executor = this
        with(scope) {
            launch {
                executeAction(action, state)
            }
            actionDelegates.forEach {
                launch {
                    it.executeAction(
                        action,
                        executor
                    )
                }
            }
        }
    }

    /**
     * Called for every `Action` produced by the [Executor]
     * The coroutine is launched in a scope which closes when the [Executor] is disposed.
     *
     * @param action an `Action` produced by the [Bootstrapper]
     * @param getState a `State` supplier that returns the *current* `State` of the [Store]
     */
    @MainThread
    protected open suspend fun executeAction(action: Action, getState: () -> State) {
    }

    override fun dispose() {
        scope.cancel()
    }

    /**
     * Dispatches the provided `Result` to the [Reducer].
     * The updated `State` will be available immediately after this method returns.
     *
     * @param result a `Result` to be dispatched to the `Reducer`
     */
    suspend fun dispatch(result: Result) = withContext(dispatchersProvider.main) {
        callbacks.requireValue().onResult(result)
    }

    /**
     * Sends the provided `Label` to the [Store] for publication
     *
     * @param label a `Label` to be published
     */
    suspend fun publish(label: Label) = withContext(dispatchersProvider.main) {
        callbacks.requireValue().onLabel(label)
    }

    protected open fun onErrorOccur(throwable: Throwable) {
        Timber.e(throwable)
    }

    protected open suspend fun processError(error: Throwable) {
        onErrorOccur(error)
    }
}
