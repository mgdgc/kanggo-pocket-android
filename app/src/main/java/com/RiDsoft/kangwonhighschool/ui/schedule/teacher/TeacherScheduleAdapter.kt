package com.RiDsoft.kangwonhighschool.ui.schedule.teacher

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.RiDsoft.kangwonhighschool.R
import java.util.ArrayList


class TeacherScheduleAdapter(val context: Context) : RecyclerView.Adapter<TeacherScheduleViewHolder>() {

    companion object {
        private const val ROW_TEACHER_SCHEDULE = R.layout.row_teacher_schedule
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int)
    }

    private var onItemClickListener: OnItemClickListener? = null
    private var onItemLongClickListener: OnItemLongClickListener? = null

    private var data: ArrayList<TeacherScheduleObject> = ArrayList()

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        this.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                listener(position)
            }

        }
    }

    fun setOnItemLongClickListener(listener: (Int) -> Unit) {
        this.onItemLongClickListener = object : OnItemLongClickListener {
            override fun onItemLongClick(position: Int) {
                listener(position)
            }

        }
    }

    fun setData(data: Array<TeacherScheduleObject>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherScheduleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewType, parent, false)
        return TeacherScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeacherScheduleViewHolder, position: Int) {
        val data = this.data[position]

        val schedule = data.subject
        val memo = data.memo
        val time = data.period
        val classInfo = data.classNum

        var content = schedule
        if (classInfo != "") {
            content += " / $classInfo"
        }

        holder.timeView.text = "${time + 1}교시"
        holder.contentView.text = content

        holder.layout.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }

        holder.layout.setOnLongClickListener {
            onItemLongClickListener?.onItemLongClick(position)
            true
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        return ROW_TEACHER_SCHEDULE
    }
}