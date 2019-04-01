package com.RiDsoft.kangwonhighschool.ui.web

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.LevelListDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Html
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.ui.web.adapter.AttachmentAdapter
import com.RiDsoft.kangwonhighschool.ui.web.worker.WebDocTask

import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

/**
 * Created by RiD on 2017. 7. 10..
 */

class WebDocActivity : AppCompatActivity() {

    private var url: String? = null
    private var fileURL: String? = null
    private var fileName: String? = null
    private var mAttaches: List<String>? = null
    private var mLinks: List<String>? = null

    private lateinit var mTitleView: TextView
    private lateinit var mDateView: TextView
    private lateinit var mContentView: TextView
    private lateinit var mSwipeLayout: SwipeRefreshLayout
    private lateinit var mAttachLayout: LinearLayout

    private lateinit var mRecyclerView: RecyclerView
    private var mAdapter: RecyclerView.Adapter<*>? = null

    private var mWebTask: WebTask? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_document)
        val toolbar = findViewById<View>(R.id.toolbar_notification_detail) as Toolbar
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }

        getIntentData()

        mTitleView = findViewById<View>(R.id.txt_noti_detail_title) as TextView
        mDateView = findViewById<View>(R.id.txt_noti_detail_date) as TextView
        mContentView = findViewById<View>(R.id.noti_detail_content) as TextView
        mSwipeLayout = findViewById<View>(R.id.swipe_refresh_noti_detail) as SwipeRefreshLayout
        mAttachLayout = findViewById<View>(R.id.layout_attachment_file) as LinearLayout
        mRecyclerView = findViewById<View>(R.id.recycler_view_attachment) as RecyclerView

        mSwipeLayout.setOnRefreshListener {
            doTheTask()
            setAttachmentLayout()
        }

        doTheTask()

        initRecyclerView()
        setAttachmentLayout()
    }

    private fun getIntentData() {
        val intent = intent
        url = intent.getStringExtra(KEY_URL)
    }

    private fun doTheTask() {
        mWebTask = WebTask()
        mWebTask!!.execute(url)
    }

    private fun initRecyclerView() {
        mAdapter = AttachmentAdapter(this, mAttaches, mLinks) //mAdapter = null, mLink = null
        (mAdapter as AttachmentAdapter).setUICallBack { _, url, fileName ->
            this@WebDocActivity.fileName = fileName
            fileURL = url

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                    startFileDownloadService(fileName)

                } else {

                    Snackbar.make(mSwipeLayout, "저장소 접근 권한이 필요합니다.", Snackbar.LENGTH_INDEFINITE)
                            .setAction("허가") {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                            REQ_PER_CODE)
                                } else {
                                    startFileDownloadService(fileName)
                                }
                            }.show()
                }

            } else {

                startFileDownloadService(fileName)
            }
        }
        mRecyclerView.adapter = mAdapter
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_PER_CODE && permissions[0] == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startFileDownloadService(fileName)

            } else {
                Snackbar.make(mSwipeLayout, "파일을 저장하려면 저장소에 쓰기 권한이 필요합니다.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("닫기") {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                                        REQ_PER_CODE)
                            } else {
                                startFileDownloadService(fileName)
                            }
                        }
                        .show()
            }
        }
    }

    private fun startFileDownloadService(fileName: String?) {
        val intent = Intent(this@WebDocActivity, FileDownloadService::class.java)
        intent.putExtra(FileDownloadService.KEY_URL, fileURL)
        intent.putExtra(FileDownloadService.KEY_FILE_NAME, fileName)
        startService(intent)
    }

    private fun setAttachmentLayout() {
        if (mAttaches == null) {
            mAttachLayout.visibility = View.GONE
        } else {
            mAttachLayout.visibility = View.VISIBLE
        }
    }

    private fun openSite() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_noti_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when (item.itemId) {
            android.R.id.home -> finish()

            R.id.action_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.putExtra(Intent.EXTRA_TEXT, mTitleView.text.toString() + "\n\n" + url)
                intent.putExtra(Intent.EXTRA_TITLE, "url 공유")
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "url 공유"))
            }

            R.id.action_original -> openSite()
        }

        return true
    }

    private inner class WebTask : WebDocTask() {

        override fun onPreProceed() {
            if (!mSwipeLayout.isRefreshing) {
                mSwipeLayout.isRefreshing = true
            }
        }

        override fun onUpdate(progress: Int) {

        }

        override fun onFinish(result: Int) {

            if (result == 0) {
                mSwipeLayout.isRefreshing = false
                mTitleView.text = title

                val spanned = Html.fromHtml(content, Html.ImageGetter { s ->
                    val d = LevelListDrawable()
                    d.addLevel(0, 0, null)
                    d.setBounds(0, 0, 0, 0)

                    LoadImage().execute(s, d)
                    d
                }, null)

                mContentView.text = spanned

                mDateView.text = "$writer | $date"
                mAttaches = attach
                mLinks = attachLink
                setAttachmentLayout()
                initRecyclerView()
                mAdapter!!.notifyDataSetChanged()

            } else {
                Snackbar.make(mSwipeLayout, "오류가 발생했습니다.", Snackbar.LENGTH_INDEFINITE)
                        .setAction("닫기") { this@WebDocActivity.finish() }.show()
            }
        }

    }

    internal inner class LoadImage : AsyncTask<Any, Void, Bitmap>() {

        private var mDrawable: LevelListDrawable? = null

        override fun doInBackground(vararg params: Any): Bitmap? {
            val source = params[0] as String
            mDrawable = params[1] as LevelListDrawable
            try {
                val `is` = URL(source).openStream()
                return BitmapFactory.decodeStream(`is`)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }

        override fun onPostExecute(bitmap: Bitmap?) {
            if (bitmap != null) {
                val d = BitmapDrawable(bitmap)
                mDrawable!!.addLevel(1, 1, d)
                mDrawable!!.setBounds(0, 0, bitmap.width, bitmap.height)
                mDrawable!!.level = 1

                val charSequence = mContentView.text
                mContentView.text = charSequence
            }
        }

    }

    companion object {

        val KEY_URL = "document_url"
        private val REQ_PER_CODE = 300
    }
}
