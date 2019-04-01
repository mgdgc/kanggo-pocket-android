package com.RiDsoft.kangwonhighschool.ui.web.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView

import com.RiDsoft.kangwonhighschool.R

/**
 * Created by RiD on 2016. 12. 22..
 */

class AttachmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val attachLayout: FrameLayout = itemView.findViewById<View>(R.id.layout_attach) as FrameLayout
    val attachTxt: TextView = itemView.findViewById<View>(R.id.txt_attach) as TextView

}
