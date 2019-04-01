package com.RiDsoft.kangwonhighschool.ui.schedule.teacher

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.RiDsoft.kangwonhighschool.R


class TeacherScheduleViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    val layout: ViewGroup = view.findViewById(R.id.layout_teacher_schedule)
    val timeView: TextView = view.findViewById(R.id.txt_schedule_time)
    val contentView: TextView = view.findViewById(R.id.txt_schedule_content)

}