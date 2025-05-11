package com.common.extensions.view

import android.os.SystemClock
import android.view.View

inline fun View.setOnClickWithDoubleCheck(
    time: Long = 400L,
    crossinline callback: (view: View) -> Unit
) {
    var shareClickTime = 0L

    setOnClickListener {
        if (SystemClock.elapsedRealtime() - shareClickTime < time) return@setOnClickListener
        shareClickTime = SystemClock.elapsedRealtime()

        callback(it)
    }
}
