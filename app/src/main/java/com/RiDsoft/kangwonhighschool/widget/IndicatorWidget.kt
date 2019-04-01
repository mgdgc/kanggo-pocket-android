package com.RiDsoft.kangwonhighschool.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout

import com.RiDsoft.kangwonhighschool.R

/**
 * Created by RiD on 2016. 10. 6..
 */

class IndicatorWidget : LinearLayout {

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    private fun initView(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        addView(getImageVIew(inflater, "indicator_1"))
        addView(getImageVIew(inflater, "indicator_2"))
    }

    private fun getImageVIew(inflater: LayoutInflater, tag: String): ImageView {
        val imageView = inflater.inflate(R.layout.view_indicator, this, false) as ImageView
        imageView.tag = tag
        return imageView
    }

    fun setSelect(position: Int) {
        val img1 = findViewWithTag("indicator_1") as ImageView
        val img2 = findViewWithTag("indicator_2") as ImageView

        img1.isSelected = position == 0
        img2.isSelected = position == 1
    }
}
