package com.mvi

import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.arkivanov.mvikotlin.rx.Disposable
import com.arkivanov.mvikotlin.rx.Observer
import com.arkivanov.mvikotlin.rx.internal.Subject

fun <T> ComponentActivity.observe(subject: Subject<T>, onChanged: (T) -> Unit) {
    subject.observe(this, onChanged)
}

fun <T> Fragment.observe(subject: Subject<T>, onChanged: (T) -> Unit) {
    subject.observe(viewLifecycleOwner, onChanged)
}

private fun <T> Subject<T>.observe(owner: LifecycleOwner, onChanged: (T) -> Unit): Disposable {
    return SubjectObserver(owner, onChanged, this).also {
        owner.lifecycle.addObserver(it)
    }
}

private class SubjectObserver<T>(
    private val owner: LifecycleOwner,
    private val observer: (T) -> Unit,
    private val subject: Subject<T>
) : DefaultLifecycleObserver, Disposable {
    private var disposable: Disposable? = null

    override val isDisposed: Boolean
        get() = disposable?.isDisposed ?: false

    override fun onStart(owner: LifecycleOwner) {
        subscribe()
        super.onStart(owner)
    }

    override fun onStop(owner: LifecycleOwner) {
        unsubscribe()
        super.onStop(owner)
    }

    private fun subscribe() {
        disposable = subject.subscribe(object : Observer<T> {
            override fun onComplete() {
                owner.lifecycle.removeObserver(this@SubjectObserver)
            }

            override fun onNext(value: T) {
                observer(value)
            }
        })
    }

    private fun unsubscribe() {
        disposable?.dispose()
    }

    override fun dispose() {
        owner.lifecycle.removeObserver(this)
        unsubscribe()
    }
}
