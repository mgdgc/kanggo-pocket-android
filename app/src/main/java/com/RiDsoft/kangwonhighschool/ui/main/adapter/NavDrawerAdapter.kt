package com.RiDsoft.kangwonhighschool.ui.main.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.ui.main.viewholder.FooterViewHolder
import com.RiDsoft.kangwonhighschool.ui.main.viewholder.NavDrawerViewHolder

import java.util.ArrayList

/**
 * Created by RiD on 2016. 12. 4..
 */

class NavDrawerAdapter(private val mContext: Context, memos: ArrayList<String>, private val mUICallBack: UICallBack) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mMemos = ArrayList<String>()

    interface UICallBack {
        fun onLongClick(position: Int, content: String)
        fun onClick(position: Int)
    }

    init {
        mMemos = memos
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(viewType, parent, false)

        val viewHolder: RecyclerView.ViewHolder
        viewHolder = if (viewType == ROW_DASHBOARD) {
            NavDrawerViewHolder(view)
        } else {
            FooterViewHolder(view)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == ROW_DASHBOARD) {
            val viewHolder = holder as NavDrawerViewHolder

            viewHolder.memoText.text = mMemos[position]

            viewHolder.memoCardView.setOnClickListener { mUICallBack.onClick(position) }

            viewHolder.memoCardView.setOnLongClickListener {
                mUICallBack.onLongClick(position, mMemos[position])
                true
            }
        } else if (holder.itemViewType == ROW_FOOTER) {
            val viewHolder = holder as FooterViewHolder
        }
    }

    override fun getItemCount(): Int {
        return if (mMemos == null) 1 else mMemos.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        return if (position < mMemos.size) {
            ROW_DASHBOARD
        } else {
            ROW_FOOTER
        }
    }

    companion object {

        private val ROW_DASHBOARD = R.layout.row_main_dashboard
        private val ROW_FOOTER = R.layout.row_footer
    }
}
