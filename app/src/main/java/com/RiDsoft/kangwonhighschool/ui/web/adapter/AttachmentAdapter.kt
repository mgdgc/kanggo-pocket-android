package com.RiDsoft.kangwonhighschool.ui.web.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.ui.web.viewholder.AttachmentViewHolder

/**
 * Created by RiD on 2016. 12. 22..
 */

class AttachmentAdapter(private val mContext: Context, private val mTxtData: List<String>?, private val mURLData: List<String>?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mUICallBack: UICallBack? = null

    interface UICallBack {
        fun onClick(position: Int, url: String?, fileName: String?)
    }

    fun setUICallBack(listener: (Int, String?, String?) -> Unit) {
        mUICallBack = object : UICallBack {
            override fun onClick(position: Int, url: String?, fileName: String?) {
                listener(position, url, fileName)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(viewType, parent, false)
        return AttachmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == ATTACHMENT_LAYOUT) {
            val viewHolder = holder as AttachmentViewHolder

            viewHolder.attachTxt.text = mTxtData!![position]
            viewHolder.attachLayout.setOnClickListener {
                if (mUICallBack != null) {
                    mUICallBack!!.onClick(position, mURLData?.get(position), mTxtData[position])
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        return ATTACHMENT_LAYOUT
    }

    override fun getItemCount(): Int {
        return mTxtData?.size ?: 0
    }

    companion object {

        private const val ATTACHMENT_LAYOUT = R.layout.row_web_attachment
    }
}
