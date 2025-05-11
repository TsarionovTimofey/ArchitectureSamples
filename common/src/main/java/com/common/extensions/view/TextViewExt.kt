package com.common.extensions.view

import android.widget.EditText
import android.widget.TextView
import com.common.extensions.string.removeNotDigits

var TextView.newText: CharSequence
    get() = text
    set(value) {
        if (text.toString() == value) {
            return
        }
        text = value
    }

var EditText.newTextWithMask: CharSequence
    get() = text
    set(value) {
        if (text.toString().removeNotDigits() == value) {
            return
        }
        setText(value)
    }

fun TextView.isEllipsized(): Boolean {
    return if (layout != null) {
        (layout.getEllipsisCount(lineCount - 1)) > 0
    } else {
        false
    }
}
