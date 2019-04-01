package com.RiDsoft.kangwonhighschool.ui.schedule.student

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

import com.RiDsoft.kangwonhighschool.R

/**
 * Created by RiD on 2016. 11. 20..
 */

class ScheduleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val weekTitle: TextView = itemView.findViewById<View>(R.id.textTitle2) as TextView
    val contentView: TextView = itemView.findViewById<View>(R.id.textDetail2) as TextView
    val cardView: CardView = itemView.findViewById<View>(R.id.card_view2) as CardView

}
