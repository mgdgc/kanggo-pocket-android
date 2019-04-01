package com.RiDsoft.kangwonhighschool.ui.announce.view

import android.view.LayoutInflater
import android.view.ViewGroup
import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.ui.main.viewholder.AndroidExtensionsViewHolder

class ImageListViewHolder(parent: ViewGroup)
    : AndroidExtensionsViewHolder(
        LayoutInflater.from(parent.context)
                .inflate(R.layout.row_image, parent, false))