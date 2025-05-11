package com.common.extensions

import android.graphics.Matrix
import android.media.ExifInterface


fun Matrix.setupByOrientation(orientation: Int) {
    when (orientation) {
        ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> {
            this.setScale(-1f, 1f)
        }
        ExifInterface.ORIENTATION_ROTATE_180 -> {
            this.setRotate(180f)
        }
        ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
            this.setRotate(180f)
            this.postScale(-1f, 1f)
        }
        ExifInterface.ORIENTATION_TRANSPOSE -> {
            this.setRotate(90f)
            this.postScale(-1f, 1f)
        }
        ExifInterface.ORIENTATION_ROTATE_90 -> {
            this.setRotate(90f)
        }
        ExifInterface.ORIENTATION_TRANSVERSE -> {
            this.setRotate(-90f)
            this.postScale(-1f, 1f)
        }
    }
}