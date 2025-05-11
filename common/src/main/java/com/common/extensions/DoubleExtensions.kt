package com.common.extensions

import java.text.DecimalFormat

fun Double.percentageOf(total: Double): Double {
    return if (this == 0.0) 0.0
    else this.times(100).div(total)
}

fun Double.format(digitsAfterDot: Int): String {
    return DecimalFormat("0.##").format(this)
}
