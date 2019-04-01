package com.RiDsoft.kangwonhighschool.ui.announce.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem
import com.RiDsoft.kangwonhighschool.R

import kotlinx.android.synthetic.main.activity_image.*

class ImageActivity : AppCompatActivity() {

    private var fileId: String? = null
    private var data: ArrayList<ImageObject>? = null
    private lateinit var adapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        initIntent()

        fileId?.let {
            parseBitmap()
        }

        adapter = ImageAdapter(this)
        vp_image.adapter = adapter
    }

    private fun initIntent() {
        val intent = intent ?: return
        fileId = intent.getStringExtra("fileId")
    }

    private fun parseBitmap() {
        val task = ImageTask()
        task.setUICallBack({

        }, { result ->
            result?.let {
                data?.clear()
                data?.addAll(result)
                adapter.setData(result)
            }
        })
        task.execute(fileId)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (data != null) {
            for (i in 0 until data!!.size) {
                if (!data!![i].image.isRecycled) {
                    data!![i].image.recycle()
                }
            }
        }
        adapter.recycle()
    }
}
