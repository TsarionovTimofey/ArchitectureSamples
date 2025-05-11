package com.common.extensions.view

import android.text.TextPaint
import android.text.style.ClickableSpan
import androidx.annotation.ColorInt

abstract class TouchableSpan(
    @ColorInt private val normalTextColor: Int? = null,
    @ColorInt private val pressedTextColor: Int? = null,
    private val isUnderLine: Boolean = true
) : ClickableSpan() {

    private var isPressed: Boolean = false

    open fun setPressed(isSelected: Boolean) {
        isPressed = isSelected
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        if (normalTextColor != null && pressedTextColor != null) {
            ds.color = if (isPressed) pressedTextColor else normalTextColor
        }
        ds.isUnderlineText = isUnderLine
    }
}
