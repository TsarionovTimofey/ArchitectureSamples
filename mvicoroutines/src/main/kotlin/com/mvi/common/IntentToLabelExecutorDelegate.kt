package com.mvi.common

class IntentToLabelExecutorDelegate<Intent : Any, State : Any, Result : Any, Label : Any>(
    private val fromIntent: Intent,
    private val toLabel: Label
) : IntentExecutorDelegate<Intent, State, Result, Label> {

    override suspend fun executeIntent(intent: Intent, executor: SuspendIntentExecutor<Intent, State, Result, Label>) {
        if (intent == fromIntent) executor.publish(toLabel)
    }
}
