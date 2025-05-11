package com.mvi.store

import com.arkivanov.mvikotlin.core.annotations.MainThread
import com.arkivanov.mvikotlin.core.store.Bootstrapper
import com.arkivanov.mvikotlin.core.store.Executor
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.utils.assertOnMainThread
import com.arkivanov.mvikotlin.rx.Disposable
import com.arkivanov.mvikotlin.rx.Observer
import com.arkivanov.mvikotlin.rx.internal.BehaviorSubject
import com.arkivanov.mvikotlin.rx.internal.PublishSubject
import com.arkivanov.mvikotlin.rx.internal.Subject
import com.arkivanov.mvikotlin.rx.observer

internal class UnicastLabelStore<
    in Intent : Any,
    in Action : Any,
    in Result : Any,
    out State : Any,
    Label : Any>
@MainThread constructor(
    initialState: State,
    private val bootstrapper: Bootstrapper<Action>?,
    executorFactory: () -> Executor<Intent, Action, State, Result, Label>,
    private val reducer: Reducer<State, Result>
) : Store<Intent, State, Label> {

    init {
        assertOnMainThread()
    }

    private val intentSubject = PublishSubject<Intent>()
    private val stateSubject = BehaviorSubject(initialState)
    override val state: State get() = stateSubject.value
    override val isDisposed: Boolean get() = !stateSubject.isActive
    private val labelSubject = UnicastSubject<Label>()
    private val executor = executorFactory()

    init {
        intentSubject.subscribe(observer(onNext = ::onIntent))

        executor.init(
            object : Executor.Callbacks<State, Result, Label> {
                override val state: State get() = stateSubject.value

                override fun onResult(result: Result) {
                    assertOnMainThread()

                    doIfNotDisposed {
                        changeState { oldState ->
                            reducer.run { oldState.reduce(result) }
                        }
                    }
                }

                override fun onLabel(label: Label) {
                    assertOnMainThread()

                    labelSubject.onNext(label)
                }
            }
        )

        bootstrapper?.init { action ->
            assertOnMainThread()

            doIfNotDisposed {
                executor.handleAction(action)
            }
        }

        bootstrapper?.invoke()
    }

    private inline fun changeState(func: (State) -> State) {
        stateSubject.onNext(func(stateSubject.value))
    }

    override fun states(observer: Observer<State>): Disposable {
        assertOnMainThread()

        return stateSubject.subscribe(observer)
    }

    override fun labels(observer: Observer<Label>): Disposable {
        assertOnMainThread()

        return labelSubject.subscribe(observer)
    }

    override fun accept(intent: Intent) {
        assertOnMainThread()

        intentSubject.onNext(intent)
    }

    private fun onIntent(intent: Intent) {
        doIfNotDisposed {
            executor.handleIntent(intent)
        }
    }

    override fun dispose() {
        assertOnMainThread()

        doIfNotDisposed {
            bootstrapper?.dispose()
            executor.dispose()
            intentSubject.onComplete()
            stateSubject.onComplete()
            labelSubject.onComplete()
        }
    }

    private inline fun doIfNotDisposed(block: () -> Unit) {
        if (!isDisposed) {
            block()
        }
    }
}

class UnicastSubject<T>(initialValue: T? = null) : Subject<T> {

    private val values: MutableList<T> = if (initialValue != null) {
        mutableListOf(initialValue)
    } else {
        mutableListOf()
    }

    private val observerConsumers = mutableListOf<Observer<T>>()

    override val isActive: Boolean
        get() = observerConsumers.isNotEmpty()

    private inline fun safeIterate(action: (Observer<T>) -> Unit) {
        for (i in observerConsumers.indices.reversed()) {
            action(observerConsumers[i])
        }
    }

    override fun onComplete() {
        safeIterate {
            it.onComplete()
        }
    }

    override fun onNext(value: T) {
        if (observerConsumers.isEmpty()) {
            values.add(value)
        } else {
            safeIterate {
                it.onNext(value)
            }
        }
    }

    override fun subscribe(observer: Observer<T>): Disposable {
        observerConsumers.add(0, observer)
        values.forEach {
            observer.onNext(it)
        }
        values.clear()
        return object : Disposable {
            override var isDisposed: Boolean = false
                private set

            override fun dispose() {
                observerConsumers.remove(observer)
                isDisposed = true
            }
        }
    }
}
