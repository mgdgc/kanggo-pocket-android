package com.RiDsoft.kangwonhighschool.ui.web.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

import com.RiDsoft.kangwonhighschool.R

/**
 * Created by RiD on 2017. 7. 11..
 */

class AddAttachmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val attachLayout: LinearLayout = itemView.findViewById<View>(R.id.layout_add_attach) as LinearLayout
    val textView: TextView = itemView.findViewById<View>(R.id.txt_attach_path) as TextView

}
