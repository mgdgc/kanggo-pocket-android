package com.RiDsoft.kangwonhighschool.ui.announce.view

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.RiDsoft.kangwonhighschool.R
import kotlinx.android.synthetic.main.layout_image.view.*
import uk.co.senab.photoview.PhotoViewAttacher

class ImageAdapter(val context: Context) : PagerAdapter() {

    private var data: ArrayList<ImageObject> = ArrayList()

    fun setData(data: ArrayList<ImageObject>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return view == obj as? View
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.layout_image, container, false)
        view.img_view.setImageBitmap(data[position].image)
        PhotoViewAttacher(view.img_view)
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.invalidate()
    }

    fun recycle() {
        for (i in 0 until data.size) {
            if (!data[i].image.isRecycled) {
                data[i].image.recycle()
            }
        }
    }
}