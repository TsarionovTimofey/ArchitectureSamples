package com.common.extensions

import android.content.res.Resources
import android.util.TypedValue
import kotlin.math.absoluteValue


fun Int.isMonthValid() : Boolean =  this in 1..12

val Int.toPx get() = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    this.toFloat(),
    Resources.getSystem().displayMetrics)

fun Int.ordinalSuffix() : String{
    val iAbs = this.absoluteValue // if you want negative ordinals, or just use i
    return "$this" + if (iAbs % 100 in 11..13) "th" else when (iAbs % 10) {
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
    }
}

