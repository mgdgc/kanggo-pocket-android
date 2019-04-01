package com.RiDsoft.kangwonhighschool.ui.main.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

import com.RiDsoft.kangwonhighschool.R

/**
 * Created by RiD on 2016. 12. 4..
 */

class NavDrawerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val memoCardView: LinearLayout = itemView.findViewById<View>(R.id.layout_dashboard_memo) as LinearLayout
    val memoText: TextView = itemView.findViewById<View>(R.id.txt_row_dashboard) as TextView

}
