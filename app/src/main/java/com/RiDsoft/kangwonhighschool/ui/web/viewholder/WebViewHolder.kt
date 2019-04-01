package com.RiDsoft.kangwonhighschool.ui.web.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView

import com.RiDsoft.kangwonhighschool.R

/**
 * Created by RiD on 2017. 7. 9..
 */

class WebViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val titleView: TextView = itemView.findViewById<View>(R.id.txt_notification_title) as TextView
    val dateView: TextView = itemView.findViewById<View>(R.id.txt_notification_date) as TextView
    val writerView: TextView = itemView.findViewById<View>(R.id.txt_notification_writer) as TextView
    val viewCounterView: TextView = itemView.findViewById<View>(R.id.txt_notification_viewed_number) as TextView
    val listItem: FrameLayout = itemView.findViewById<View>(R.id.layout_notification_item) as FrameLayout

}
