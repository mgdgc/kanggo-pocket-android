package com.RiDsoft.kangwonhighschool.ui.web

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.net.Uri
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.widget.Toast

import com.RiDsoft.kangwonhighschool.R

/**
 * Created by RiD on 2017. 1. 16..
 */

class FileDownloadService : Service() {

    private var url: String? = null
    private var fileName: String? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        url = intent.getStringExtra(KEY_URL)
        fileName = intent.getStringExtra(KEY_FILE_NAME)

        val task = FileDownloadTask()
        task.execute(url, fileName)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun getIntentType(fileName: String?): String {

        return if (fileName != null) {
            if (fileName.contains("mp3")) {
                "audio/*"
            } else if (fileName.contains("mp4")) {
                "video/*"
            } else if (fileName.contains("jpg") || fileName.contains("jpeg")
                    || fileName.contains("gif") || fileName.contains("png")
                    || fileName.contains("bmp")) {
                "image/*"
            } else if (fileName.contains("txt")) {
                "text/*"
            } else if (fileName.contains("doc") || fileName.contains("docx")) {
                "application/msword"
            } else if (fileName.contains("xls") || fileName.contains("xlsx")) {
                "application/vnd.ms-excel"
            } else if (fileName.contains("ppt") || fileName.contains("pptx")) {
                "application/vnd.ms-powerpoint"
            } else if (fileName.contains("pdf")) {
                "application/pdf"
            } else if (fileName.contains("hwp")) {
                "application/haansofthwp"
            } else {
                "application/*"
            }
        } else {
            "application/*"
        }

    }

    inner class FileDownloadTask : com.RiDsoft.kangwonhighschool.ui.web.worker.FileDownloadTask() {

        override fun onPreProceed() {
            Toast.makeText(this@FileDownloadService, "파일을 다운로드합니다.", Toast.LENGTH_LONG).show()
        }

        override fun onUpdate(progress: Int) {

        }

        override fun onFinish(vararg result: String) {

            if (result[0] != null) {

                val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.type = getIntentType(result[0])
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(result[0]))
                val pendingIntent = PendingIntent.getActivity(
                        this@FileDownloadService, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

                val builder = NotificationCompat.Builder(this@FileDownloadService)
                builder.setContentTitle("파일 다운로드 완료")
                        .setContentText("파일 다운로드 완료")
                        .setTicker("파일 다운로드 완료")
                        .setSmallIcon(R.drawable.ic_file_download_white_24dp)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setWhen(System.currentTimeMillis())
                        .setDefaults(Notification.DEFAULT_ALL)

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    builder.setCategory(Notification.CATEGORY_SERVICE)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setVisibility(Notification.VISIBILITY_PUBLIC)
                }

                manager.notify(121, builder.build())
            } else {
                Toast.makeText(this@FileDownloadService, "파일을 다운로드하는 중에 오류가 발생했습니다.", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {

        val KEY_URL = "url"
        val KEY_FILE_NAME = "file_name"
    }
}
