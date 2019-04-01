package com.RiDsoft.kangwonhighschool.ui.schedule

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast

import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.worker.ScheduleParser

/**
 * Created by RiD on 2017. 3. 1..
 */

class ScheduleDownloadActivity : Activity() {

    private var txtErrorMsg: TextView? = null
    private var isDownloadFinished = false

    private val isConnected: Boolean
        get() {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null
        }

    override fun onBackPressed() {
        if (isDownloadFinished) {
            super.onBackPressed()
        } else {
            Toast.makeText(this, "시간표를 다운로드하고 있습니다. 잠시만 기다려 주세요 :)", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_download)

        val progressBar = findViewById<ProgressBar>(R.id.pb_schedule_download)
        txtErrorMsg = findViewById(R.id.txt_download_error_msg)

        if (isConnected) {
            val task = ScheduleDownloadTask(this)
            task.setOnDownloadStartListener(object : ScheduleDownloadTask.OnDownloadStartListener {
                override fun onDownloadStart() {

                }
            })
            task.setOnDownloadFinishListener(object : ScheduleDownloadTask.OnDownloadFinishListener {
                override fun onDownloadFinished(result: Int) {
                    if (result == 1) {
                        val dialog = AlertDialog.Builder(this@ScheduleDownloadActivity)
                        dialog.setTitle("시간표 다운로드 실패")
                                .setMessage("시간표 소스를 다운로드할 수 없습니다. 개발자에게 문의해 주십시오.")
                                .setPositiveButton("닫기") { dialog, which ->
                                    dialog.dismiss()
                                    finish()
                                }
                                .show()
                        isDownloadFinished = true
                    } else {
                        isDownloadFinished = true
                        finish()
                    }
                }
            })
            task.execute()
        } else {
            progressBar.visibility = View.INVISIBLE
            val builder = AlertDialog.Builder(this)
            builder.setTitle("네트워크 연결 안됨")
                    .setMessage("연결된 네트워크가 없습니다. 나중에 다시 시도해 주세요.")
                    .setCancelable(false)
                    .setPositiveButton("확인") { dialogInterface, i ->
                        dialogInterface.dismiss()
                        this@ScheduleDownloadActivity.finish()
                    }
                    .show()
        }

        val handler = Handler()
        handler.postDelayed({
            txtErrorMsg!!.text = "네트워크가 지연되고 있습니다..."
            isDownloadFinished = true
        }, 10000)

    }

    class ScheduleDownloadTask(private val context: Context) : AsyncTask<Void, Int, Int>() {

        private var onDownloadStartListener: OnDownloadStartListener? = null
        private var onDownloadFinishListener: OnDownloadFinishListener? = null

        interface OnDownloadStartListener {
            fun onDownloadStart()
        }

        interface OnDownloadFinishListener {
            fun onDownloadFinished(result: Int)
        }

        fun setOnDownloadStartListener(onDownloadStartListener: OnDownloadStartListener) {
            this.onDownloadStartListener = onDownloadStartListener
        }

        fun setOnDownloadFinishListener(onDownloadFinishListener: OnDownloadFinishListener) {
            this.onDownloadFinishListener = onDownloadFinishListener
        }

        override fun onPreExecute() {
            super.onPreExecute()
            if (onDownloadStartListener != null) {
                onDownloadStartListener!!.onDownloadStart()
            }
        }

        override fun doInBackground(vararg voids: Void): Int? {
            val parser = ScheduleParser(context)
            val scheduleVersion = parser.version
            val scheduleTable = parser.getScheduleTable() ?: return 1
            val classNumData = parser.classNumData
            return parser.saveScheduleData(scheduleVersion, scheduleTable, classNumData)
        }

        override fun onPostExecute(result: Int?) {
            super.onPostExecute(result)
            if (onDownloadFinishListener != null) {
                onDownloadFinishListener!!.onDownloadFinished(result!!)
            }
        }
    }

}
