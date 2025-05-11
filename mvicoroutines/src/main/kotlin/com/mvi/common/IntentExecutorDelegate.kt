package com.mvi.common

import com.arkivanov.mvikotlin.core.annotations.MainThread

interface IntentExecutorDelegate<Intent : Any, State : Any, Result : Any, Label : Any> {
    @MainThread
    suspend fun executeIntent(
        intent: Intent,
        executor: SuspendIntentExecutor<Intent, State, Result, Label>
    )
}

interface ActionExecutorDelegate<Action : Any, State : Any, Result : Any, Label : Any> {
    @MainThread
    suspend fun executeAction(
        action: Action,
        executor: SuspendActionExecutor<Action, State, Result, Label>
    )
}
