package com.mvi

import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.extensions.coroutines.events
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.common.coroutines.DispatchersProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
abstract class SimpleBinder<
    Intent : Any,
    State : Any,
    Label : Any,
    View : BaseView<State, Intent>
    >(store: Store<Intent, State, Label>, dispatchersProvider: DispatchersProvider) :
    BaseBinder<Intent, State, Label, Intent, State, View>(store, dispatchersProvider) {
    override fun modelFlow() = store.states
    override fun labelFlow() = store.labels
    override fun intentFlow(view: View) = view.events
}
