package com.mvi.binding

import android.content.Context
import androidx.viewbinding.ViewBinding
import com.arkivanov.mvikotlin.core.annotations.MainThread
import com.arkivanov.mvikotlin.core.view.MviView
import com.arkivanov.mvikotlin.rx.Disposable
import com.arkivanov.mvikotlin.rx.Observer
import com.mvi.store.UnicastSubject

/**
 * Prevent memory leak when bindingViewImpl stored in fragment lateinit field
 */
abstract class BindingView<View : ViewBinding, in Model : Any, Event : Any>(
    private val binding: () -> View
) : MviView<Model, Event> {
    protected val context: Context
        get() = binding().root.context
    private val subject = UnicastSubject<Event>()

    override fun events(observer: Observer<Event>): Disposable = subject.subscribe(observer)

    @MainThread
    fun dispatch(event: Event) = subject.onNext(event)

    final override fun render(model: Model) = render(binding(), model)

    abstract fun render(binding: View, model: Model)
}
