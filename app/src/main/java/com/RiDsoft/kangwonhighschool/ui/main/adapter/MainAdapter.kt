package com.RiDsoft.kangwonhighschool.ui.main.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView

import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.ui.main.viewholder.MainShortcutViewHolder
import com.RiDsoft.kangwonhighschool.ui.main.viewholder.MainViewHolder
import com.RiDsoft.kangwonhighschool.worker.UITool
import java.util.*

/**
 * Created by RiD on 2017. 4. 9..
 */

class MainAdapter(var context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data = ArrayList<MainActivityObject>()


    fun setData(data: Array<MainActivityObject>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    fun addData(data: MainActivityObject, position: Int = this.data.size) {
        this.data.add(position, data)
        notifyItemInserted(position)
    }

    fun addData(data: Array<MainActivityObject>) {
        val size = this.data.size
        this.data.addAll(data)
        notifyItemRangeInserted(size, data.size)
    }

    fun removeData(position: Int, itemCount: Int = 1) {
        for (i in 0 until itemCount) {
            this.data.removeAt(position)
        }
        notifyItemRangeRemoved(position, itemCount)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == ROW_DETAIL) {
            val view = inflater.inflate(ROW_DETAIL, parent, false)
            MainViewHolder(view)
        } else {
            val view = inflater.inflate(ROW_MENU, parent, false)
            MainShortcutViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.e("MainAdapter", position.toString())
        val data = this.data[position]

        if (holder.itemViewType == ROW_DETAIL) {
            val viewHolder = holder as MainViewHolder

            viewHolder.title.text = data.title

            viewHolder.content.text = data.content
            setTextViewAttr(viewHolder.content)

            viewHolder.icon.setImageBitmap(data.icon)

            viewHolder.cardView.setOnClickListener {
                data.action()
            }

            if (holder.title.text == context.getString(R.string.menu_schedule)) {
                viewHolder.title.setTextColor(Color.parseColor("#009688"))
            } else if (holder.title.text == context.getString(R.string.menu_meal)) {
                viewHolder.title.setTextColor(Color.parseColor("#ff5722"))
            }

        } else {
            val viewHolder = holder as MainShortcutViewHolder

            viewHolder.title.text = data.title
            viewHolder.icon.setImageBitmap(data.icon)

            viewHolder.cardView.setOnClickListener {
                data.action()
            }
        }
    }

    private fun setTextViewAttr(view: TextView) {
        val tool = UITool(context)
        tool.setTextSize(view)
    }

    override fun getItemCount(): Int {
        return this.data.size
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        return data[position].viewType
    }

    companion object {
        const val ROW_DETAIL = R.layout.row_main_detail
        const val ROW_MENU = R.layout.row_main_shortcut
    }
}

class MainActivityObject constructor(
        var viewType: Int, var title: String, var content: String?, var icon: Bitmap?, var action: () -> Unit)