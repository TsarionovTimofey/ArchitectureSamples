package com.common

import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * Process messages only when started
 * Note that is stopped by default after creation
 */
class LCHandler(
    lifecycleOwner: LifecycleOwner,
    private var started: Boolean = false
) : Handler(Looper.getMainLooper()), DefaultLifecycleObserver {
    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        started = true
    }

    override fun onStop(owner: LifecycleOwner) {
        started = false
        removeCallbacksAndMessages(null)
    }

    override fun dispatchMessage(msg: Message) {
        if (!started) {
            return
        }
        super.dispatchMessage(msg)
    }
}
