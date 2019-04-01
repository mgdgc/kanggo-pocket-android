package com.RiDsoft.kangwonhighschool.ui.web.adapter

import android.content.Context
import android.net.Uri
import android.preference.Preference
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.ui.web.viewholder.AddAttachmentViewHolder

import java.util.ArrayList

/**
 * Created by RiD on 2017. 7. 11..
 */

class AddAttachmentAdapter(private val context: Context, data: Array<Uri>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal val data = ArrayList<Uri>()
    private var onClickListener: OnClickListener? = null

    init {
        if (data != null) {
            for (uri in data) {
                this.data.add(uri)
            }
        }
    }

    interface OnClickListener {
        fun onClick(position: Int)
    }

    fun setOnClickListener(listener: (Int) -> Unit) {
        this.onClickListener = object : OnClickListener {
            override fun onClick(position: Int) {
                listener(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(ROW_ATTACHMENT, parent, false)
        return AddAttachmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder.itemViewType == ROW_ATTACHMENT) {

            val viewHolder = holder as AddAttachmentViewHolder

            viewHolder.textView.text = data[position].toString()
            viewHolder.attachLayout.setOnLongClickListener {
                onClickListener!!.onClick(position)
                true
            }

        }

    }

    fun addData(uri: Uri) {
        data.add(data.size, uri)
        notifyDataSetChanged()
    }

    fun removeData(position: Int) {
        data.removeAt(position)
        notifyDataSetChanged()
    }

    fun getData(): Array<Uri>? {
        return data.toTypedArray()
    }

    override fun getItemViewType(position: Int): Int {
        return ROW_ATTACHMENT
    }

    override fun getItemCount(): Int {
        return data.size
    }

    companion object {

        private const val ROW_ATTACHMENT = R.layout.row_add_attachment
    }
}
