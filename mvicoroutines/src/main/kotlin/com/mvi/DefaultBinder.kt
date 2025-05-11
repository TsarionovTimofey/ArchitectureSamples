package com.mvi

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.events
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.common.coroutines.DispatchersProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalCoroutinesApi::class)
abstract class DefaultBinder<
    Intent : Any,
    State : Any,
    Label : Any,
    Event : Any,
    Model : Any,
    View : BaseView<Model, Event>
    >(store: Store<Intent, State, Label>, dispatchersProvider: DispatchersProvider) :
    BaseBinder<Intent, State, Label, Event, Model, View>(store, dispatchersProvider) {
    /**
     * Converts any feature state as State to View's Model
     */
    protected abstract val stateToModel: suspend (State.() -> Model)

    /**
     * Converts any action as Event to Store's Intent
     */
    protected abstract val eventToIntent: suspend (Event.() -> Intent)

    override fun modelFlow() = store.states.map(stateToModel)
    override fun labelFlow() = store.labels
    override fun intentFlow(view: View) = view.events.map(eventToIntent)
}
