package com.mvi

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

class MviLifecycleObserver(
    private val binder: Binder<*>,
    owner: LifecycleOwner
) : DefaultLifecycleObserver {

    init {
        owner.lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        binder.onCreate()
    }

    override fun onStart(owner: LifecycleOwner) {
        binder.onStart()
    }

    override fun onResume(owner: LifecycleOwner) {
        binder.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        binder.onPause()
    }

    override fun onStop(owner: LifecycleOwner) {
        binder.onStop()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        binder.onViewDestroyed()
    }
}
