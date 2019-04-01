package com.RiDsoft.kangwonhighschool.ui.schedule.student

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.value.DataManager

/**
 * Created by RiD on 2016. 11. 20..
 */

class ScheduleAdapter(private val mContext: Context, private val mType: Int, private val mGrade: Int, private val mClassNum: Int, private val mUICallBack: UICallBack) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface UICallBack {
        fun onItemClick(position: Int)

        fun onItemLongClick(position: Int, content: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewType, parent, false)
        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = holder.itemViewType
        if (viewType == CARDVIEW_LAYOUT) {

            val getter = DataManager(mContext)

            val viewHolder = holder as ScheduleViewHolder

            viewHolder.weekTitle.text = getWeekChar(position)
            viewHolder.contentView.text = getter.getScheduleOfWeek(mGrade, mClassNum)[position]

            viewHolder.cardView.setOnClickListener { mUICallBack.onItemClick(position) }

            viewHolder.cardView.setOnLongClickListener {
                mUICallBack.onItemLongClick(position,
                        getter.getScheduleOfWeek(mGrade, mClassNum)[position])
                true
            }

        }
    }

    private fun getWeekChar(position: Int): String? {
        return when (position) {
            0 -> "월요일"
            1 -> "화요일"
            2 -> "수요일"
            3 -> "목요일"
            4 -> "금요일"
            else -> null
        }
    }

    private fun getWeekCode(position: Int): String? {
        return when (position) {
            0 -> "mon"
            1 -> "tues"
            2 -> "wed"
            3 -> "thurs"
            4 -> "fri"
            else -> null
        }
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        return CARDVIEW_LAYOUT
    }

    companion object {

        private const val CARDVIEW_LAYOUT = R.layout.row_cardview_schedule
    }

}
