package com.RiDsoft.kangwonhighschool.ui.announce

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.ui.main.viewholder.AndroidExtensionsViewHolder

class AnnounceViewHolder(parent: ViewGroup)
    : AndroidExtensionsViewHolder(
        LayoutInflater.from(parent.context)
                .inflate(R.layout.row_announce, parent, false))