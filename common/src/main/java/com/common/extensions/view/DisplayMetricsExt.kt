package com.common.extensions.view

import android.content.res.Resources
import com.common.extensions.delegate.unsafeLazy

val systemResources: Resources by unsafeLazy { Resources.getSystem() }

val Int.px: Float
    get() = this * systemResources.displayMetrics.density

val Float.px: Float
    get() = this * systemResources.displayMetrics.density

val Float.sp: Float
    get() = this * systemResources.displayMetrics.scaledDensity
