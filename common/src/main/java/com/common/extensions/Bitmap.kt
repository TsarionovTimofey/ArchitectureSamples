package com.common.extensions

import android.graphics.Bitmap
import android.graphics.Matrix

fun Bitmap.rotate(matrix: Matrix): Bitmap =
    Bitmap.createBitmap(
        this,
        0,
        0,
        this.width,
        this.height,
        matrix,
        true
    )

