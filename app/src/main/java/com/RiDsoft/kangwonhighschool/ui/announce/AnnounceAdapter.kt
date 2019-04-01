package com.RiDsoft.kangwonhighschool.ui.announce

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import kotlinx.android.synthetic.main.row_announce.*
import java.util.ArrayList

class AnnounceAdapter : RecyclerView.Adapter<AnnounceViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int, data: AnnounceObject)
    }

    fun setOnItemClickListener(listener: (Int, AnnounceObject) -> Unit) {
        this.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(position: Int, data: AnnounceObject) {
                listener(position, data)
            }
        }
    }

    private var data : ArrayList<AnnounceObject> = ArrayList()
    private var onItemClickListener: OnItemClickListener? = null

    fun setData(data: ArrayList<AnnounceObject>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnnounceViewHolder {
        return AnnounceViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: AnnounceViewHolder, position: Int) {
        val title = data[position].title
        var detail = "작성일: ${data[position].written} | 카테고리: "
        when (data[position].category) {
            1 -> detail += "일반공지"
            2 -> detail += "학적공지"
            3 -> detail += "행사안내"
            4 -> detail += "업데이트 공지"
        }
        val viewed = data[position].viewed.toString()

        holder.txtTitle.text = title
        holder.txtDetail.text = detail
        holder.txtViewed.text = viewed

        holder.layout_announce.setOnClickListener {
            onItemClickListener?.onItemClick(position, data[position])
        }
    }

}