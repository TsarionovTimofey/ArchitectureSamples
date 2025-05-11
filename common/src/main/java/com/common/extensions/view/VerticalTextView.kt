package com.common.extensions.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView


class VerticalTextView(context: Context?, attrs: AttributeSet?) :
    AppCompatTextView(context!!, attrs) {
    var topDown = false
    var textPaint = paint

    init {
        val gravity = gravity
        topDown = if (Gravity.isVertical(gravity)
            && gravity and Gravity.VERTICAL_GRAVITY_MASK == Gravity.BOTTOM
        ) {
            setGravity(
                gravity and Gravity.HORIZONTAL_GRAVITY_MASK
                        or Gravity.TOP
            )
            false
        } else {
            true
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measuredHeight, measuredWidth)
    }

    override fun onDraw(canvas: Canvas) {
        textPaint.color = currentTextColor
        textPaint.drawableState = drawableState
        canvas.save()
        if (topDown) {
            canvas.translate(width.toFloat(), 0f)
            canvas.rotate(90f)
        } else {
            canvas.translate(0f, height.toFloat())
            canvas.rotate(-90f)
        }
        canvas.translate(compoundPaddingLeft.toFloat(), extendedPaddingTop.toFloat())
        layout.draw(canvas)
        canvas.restore()
    }
}