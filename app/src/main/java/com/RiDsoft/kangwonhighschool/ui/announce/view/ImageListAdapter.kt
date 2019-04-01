package com.RiDsoft.kangwonhighschool.ui.announce.view

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import kotlinx.android.synthetic.main.row_image.*
import java.util.ArrayList

class ImageListAdapter : RecyclerView.Adapter<ImageListViewHolder>() {

    private var data: ArrayList<ImageObject> = ArrayList()
    private var onItemClickListener: OnItemClickListener? = null

    fun setData(data: ArrayList<ImageObject>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageListViewHolder {
        return ImageListViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ImageListViewHolder, position: Int) {
        holder.img_image.setImageBitmap(data[position].image)
        holder.layout_image.setOnClickListener {
            onItemClickListener?.onItemClick(position, data[position])
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        recycle()
    }

    fun recycle() {
        data.forEach {
            it.recycle()
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, imageObject: ImageObject)
    }

    fun setOnItemClickListener(listener: (Int, ImageObject) -> Unit) {
        this.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(position: Int, imageObject: ImageObject) {
                listener(position, imageObject)
            }

        }
    }

}