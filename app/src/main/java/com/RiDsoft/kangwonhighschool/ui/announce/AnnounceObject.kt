package com.RiDsoft.kangwonhighschool.ui.announce

import com.google.gson.annotations.SerializedName

data class AnnounceObject(@SerializedName("docId") var docId: Int,
                          @SerializedName("category") var category: Int,
                          @SerializedName("title") var title: String,
                          @SerializedName("content") var content: String,
                          @SerializedName("imgUrls") var imgIds: String,
                          @SerializedName("fileUrls") var fileIds: String,
                          @SerializedName("written") var written: String,
                          @SerializedName("usrId") var usrId: String,
                          @SerializedName("viewed") var viewed: Int,
                          @SerializedName("isPrivate") var isPrivate: Boolean)