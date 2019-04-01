package com.RiDsoft.kangwonhighschool.widget

import android.content.Context
import android.graphics.Canvas
import android.widget.ImageView
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet


class RoundedCornerImageView(context: Context, attr: AttributeSet) : ImageView(context, attr) {

    var radius: Float = 16f

    private var clipPath: Path = Path()

    init {
        val rect = RectF(0f, 0f, this.width.toFloat(), this.height.toFloat())
        clipPath.addRoundRect(rect, radius, radius, Path.Direction.CW)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.clipPath(clipPath)
        super.onDraw(canvas)
    }
}