package com.common.extensions

import android.content.Context

val Context.displayHeight: Int
    get() {
        return resources.displayMetrics.heightPixels
    }
val Context.displayWidth: Int
    get() {
        return resources.displayMetrics.widthPixels
    }
