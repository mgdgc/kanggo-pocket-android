package com.RiDsoft.kangwonhighschool.ui.web.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup

import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.ui.web.viewholder.FooterViewHolder
import com.RiDsoft.kangwonhighschool.ui.web.viewholder.WebViewHolder
import com.RiDsoft.kangwonhighschool.ui.web.worker.WebValueObject

import java.util.ArrayList

/**
 * Created by RiD on 2017. 7. 9..
 */

class WebAdapter(private val context: Context, private val onClickListener: OnClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data = ArrayList<WebValueObject>()
    private lateinit var scrollListener: ScrollListener

    interface OnClickListener {
        fun onDocumentClick(id: Int)
        fun onFooterClick()
    }

    interface ScrollListener {
        fun onScrolledToBottom()
    }

    fun setScrollListener(listener: () -> Unit) {
        this.scrollListener = object : ScrollListener {
            override fun onScrolledToBottom() {
                listener()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewType, parent, false)
        return if (viewType == ROW_WEB_LIST) {
            WebViewHolder(view)
        } else {
            FooterViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (position == data.size - 3) {
            scrollListener.onScrolledToBottom()
        }

        if (holder.itemViewType == ROW_WEB_LIST) {

            val viewHolder = holder as WebViewHolder
            viewHolder.dateView.text = data[position].date
            viewHolder.writerView.text = data[position].writer
            viewHolder.viewCounterView.text = data[position].viewCount

            val title = data[position].titleText
            val spanned = Html.fromHtml(title, Html.ImageGetter { context.resources.getDrawable(R.drawable.img_empty) }, null)

            viewHolder.titleView.text = spanned
            viewHolder.listItem.setOnClickListener {
                val id = data[position].urlInfo
                onClickListener.onDocumentClick(id)
            }

        } else if (holder.itemViewType == ROW_WEB_FOOTER) {

            val viewHolder = holder as FooterViewHolder

            viewHolder.footerLayout.setOnClickListener { }
        }

    }

    fun updateData(vararg wvos: WebValueObject) {
        data.clear()
        for (wvo in wvos) {
            data.add(wvo)
        }
        notifyDataSetChanged()
    }

    fun addData(vararg wvos: WebValueObject) {
        val insertPosition = data.size
        for (wvo in wvos) {
            data.add(data.size, wvo)
        }
        notifyItemInserted(insertPosition)
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        return if (position < data.size) {
            ROW_WEB_LIST
        } else {
            ROW_WEB_FOOTER
        }
    }

    override fun getItemCount(): Int {
        return data.size + 1
    }

    companion object {

        private const val ROW_WEB_LIST = R.layout.row_web_list
        private const val ROW_WEB_FOOTER = R.layout.row_web_footer
    }
}
