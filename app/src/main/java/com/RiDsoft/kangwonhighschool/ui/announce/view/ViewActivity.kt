package com.RiDsoft.kangwonhighschool.ui.announce.view

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.ui.announce.AnnounceObject
import com.RiDsoft.kangwonhighschool.worker.AnnounceParser
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_view.*
import java.lang.Exception
import java.lang.NumberFormatException
import java.lang.StringBuilder
import java.util.ArrayList

class ImageObject(var image: Bitmap, var fileId: Int) {
    fun recycle() {
        if (!image.isRecycled) {
            image.recycle()
        }
    }
}

class AttachObject(@SerializedName("originalName") var title: String,
                   @SerializedName("fileId") var fileId: String) {
    override fun toString(): String {
        return title
    }
}

class ViewActivity : AppCompatActivity() {

    private lateinit var imageAdapter: ImageListAdapter
    private var attachData: ArrayList<AttachObject> = ArrayList()

    var announce: AnnounceObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)
        setSupportActionBar(toolbar_view)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initIntentData()
        initSwipeRefreshLayout()
        initImageListView()
        initData()

        card_view_attach.setOnClickListener {
            showAttachmentDialog()
        }
    }

    private fun initIntentData() {
        announce = Gson().fromJson<AnnounceObject>(intent?.getStringExtra("object"), AnnounceObject::class.java)
        if (announce != null) {
            supportActionBar?.title = announce?.title
        } else {
            val alert = AlertDialog.Builder(this)
            alert.setTitle("오류")
                    .setMessage("잘못된 접근입니다. 개발자에게 문의해주세요.")
                    .setPositiveButton("확인") { dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }
                    .setCancelable(false)
                    .show()
        }
    }

    private fun initSwipeRefreshLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            srl_view.setColorSchemeColors(
                    resources.getColor(R.color.refresh_orange, null),
                    resources.getColor(R.color.refresh_yellow, null),
                    resources.getColor(R.color.refresh_green, null),
                    resources.getColor(R.color.refresh_blue_grey, null)
            )
        }

        srl_view.setOnRefreshListener {
            initData()
        }
    }

    private fun initImageListView() {
        rv_view_img.setHasFixedSize(true)
        rv_view_img.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        imageAdapter = ImageListAdapter()
        imageAdapter.setOnItemClickListener { position, imageObject ->
            val intent = Intent(this@ViewActivity, ImageActivity::class.java)
            intent.putExtra("fileId", announce?.imgIds)
            startActivity(intent)
        }

        rv_view_img.adapter = imageAdapter
    }

    private fun showAttachmentDialog() {
        val attachAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, attachData)

        val alert = AlertDialog.Builder(this)
        alert.setTitle("첨부파일")
                .setAdapter(attachAdapter) { dialog, which ->
                    dialog.dismiss()
                    val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(AnnounceParser.URL_BASE + "/file/file_download.jsp?fileId=" + attachData[which].fileId))
                    try {
                        startActivity(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                .setNegativeButton("닫기") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
    }

    private fun initData() {
        if (announce != null) {
            val task = AnnouncementTask(announce!!.docId)
            task.setUICallBack({
                srl_view.isRefreshing = true
            }, { result ->
                this.announce = result
                srl_view.isRefreshing = false
                setViewData()

                // 이미지 로딩
                if (result?.imgIds != null) {
                    initImages(result.imgIds)
                }

                // 첨부파일 로딩
                if (result?.fileIds != null) {
                    initAttachments(result.fileIds)
                }
            })
            task.execute()
        }
    }

    private fun initImages(ids: String) {
        val imgTask = ImageTask()
        imgTask.setUICallBack({
            srl_view.isRefreshing = true
        }, { result ->
            srl_view.isRefreshing = false
            if (result != null && result.size > 0) {
                rv_view_img.visibility = View.VISIBLE
                imageAdapter.setData(result)
            }
        })
        imgTask.execute(ids)
    }

    private fun initAttachments(ids: String) {
        val attachTask = AttachmentTask()
        attachTask.setUICallBack({
            srl_view.isRefreshing = true
        }, { result ->
            srl_view.isRefreshing = false
            if (result != null && result.size > 0) {
                this.attachData.clear()
                this.attachData.addAll(result)
                card_view_attach.visibility = View.VISIBLE
            }
        })
        attachTask.execute(ids)
    }

    private fun setViewData() {
        if (announce != null) {
            supportActionBar?.title = announce?.title

            txt_view_title.text = announce?.title
            val builder = StringBuilder()
            builder.append("카테고리: ")
            when (announce?.category) {
                1 -> builder.append("일반공지")
                2 -> builder.append("학적공지")
                3 -> builder.append("행사안내")
                4 -> builder.append("업데이트 공지")
            }
            builder.append(" | ")
            builder.append("조회수: " + announce?.viewed)
            builder.append("\n")
            builder.append("작성일: " + announce?.written)
            txt_view_detail.text = builder.toString()
            txt_view_content.text = announce?.content
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when {
            item?.itemId == android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        imageAdapter.recycle()
    }
}

class AnnouncementTask(private val docId: Int) : AsyncTask<Void, Int, AnnounceObject?>() {

    interface UICallBack {
        fun preExecute()
        fun postExecute(result: AnnounceObject?)
    }

    private var callBack: UICallBack? = null

    fun setUICallBack(listener1: () -> Unit, listener2: (AnnounceObject?) -> Unit) {
        this.callBack = object : UICallBack {
            override fun preExecute() {
                listener1()
            }

            override fun postExecute(result: AnnounceObject?) {
                listener2(result)
            }

        }
    }

    override fun onPreExecute() {
        super.onPreExecute()
        callBack?.preExecute()
    }

    override fun doInBackground(vararg params: Void?): AnnounceObject? {
        val parser = AnnounceParser()
        val doc = parser.getDocument(AnnounceParser.URL_ANNOUNCE + "?docId=" + docId) ?: return null
        return parser.getAnnouncement(doc)
    }

    override fun onPostExecute(result: AnnounceObject?) {
        super.onPostExecute(result)
        callBack?.postExecute(result)
    }
}

class ImageTask : AsyncTask<String, Int, ArrayList<ImageObject>?>() {

    interface UICallBack {
        fun preExecute()
        fun postExecute(result: ArrayList<ImageObject>?)
    }

    private var callBack: UICallBack? = null

    fun setUICallBack(listener1: () -> Unit, listener2: (ArrayList<ImageObject>?) -> Unit) {
        this.callBack = object : UICallBack {
            override fun preExecute() {
                listener1()
            }

            override fun postExecute(result: ArrayList<ImageObject>?) {
                listener2(result)
            }

        }
    }

    override fun onPreExecute() {
        super.onPreExecute()
        callBack?.preExecute()
    }

    override fun doInBackground(vararg params: String?): ArrayList<ImageObject>? {
        val id = params[0] ?: return null
        val ids = id.split(",")

        val parser = AnnounceParser()

        val result: ArrayList<ImageObject> = ArrayList()
        for (i in ids) {
            try {
                val bitmap = parser.getImageById(i.trim().toInt())
                if (bitmap != null) {
                    val obj = ImageObject(bitmap, i.toInt())
                    result.add(obj)
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }

        return result
    }

    override fun onPostExecute(result: ArrayList<ImageObject>?) {
        super.onPostExecute(result)
        callBack?.postExecute(result)
    }
}

class AttachmentTask : AsyncTask<String, Int, ArrayList<AttachObject>?>() {

    interface UICallBack {
        fun preExecute()
        fun postExecute(result: ArrayList<AttachObject>?)
    }

    private var callBack: UICallBack? = null

    fun setUICallBack(listener1: () -> Unit, listener2: (ArrayList<AttachObject>?) -> Unit) {
        this.callBack = object : UICallBack {
            override fun preExecute() {
                listener1()
            }

            override fun postExecute(result: ArrayList<AttachObject>?) {
                listener2(result)
            }

        }
    }

    override fun onPreExecute() {
        super.onPreExecute()
        callBack?.preExecute()
    }

    override fun doInBackground(vararg params: String?): ArrayList<AttachObject>? {
        val id = params[0] ?: return null
        val ids = id.split(",")

        val parser = AnnounceParser()

        val result: ArrayList<AttachObject> = ArrayList()
        for (i in ids) {
            try {
                val obj = parser.getFileObjectById(i.trim().toInt())
                if (obj != null) {
                    result.add(obj)
                }
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }
        }

        return result
    }

    override fun onPostExecute(result: ArrayList<AttachObject>?) {
        super.onPostExecute(result)
        callBack?.postExecute(result)
    }
}