package com.RiDsoft.kangwonhighschool.ui.web.worker

import android.os.AsyncTask
import android.os.Environment
import android.util.Log
import android.webkit.CookieManager

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by RiD on 2017. 1. 16..
 */

abstract class FileDownloadTask : AsyncTask<String, Int, String>() {

    private val filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/kanggo pocket/"

    private val fileName: String
        get() {
            val df = SimpleDateFormat("yyyy-MM-dd-HH-mm_ss", Locale.KOREA)
            val str_date = df.format(Date())
            return "kp_download_$str_date"
        }

    abstract fun onPreProceed()

    abstract fun onUpdate(progress: Int)

    abstract fun onFinish(vararg result: String)

    override fun onPreExecute() {
        super.onPreExecute()
        onPreProceed()
    }

    override fun doInBackground(vararg strings: String): String? {

        //strings[0]: url, strings[1]: filename

        val urlString = strings[0]

        Log.d("download url", urlString)

        val fileName: String = strings[1]

        try {
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection

            //Timeout 시간
            connection.connectTimeout = 15000
            connection.readTimeout = 15000
            //요청 방식 설정
            connection.requestMethod = "POST"

            connection.doInput = true
            connection.useCaches = false

            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            connection.setRequestProperty("Connection", "keep-alive")

            val cookie = CookieManager.getInstance().getCookie("http://kanggo.net")
            if (cookie != null) {
                connection.setRequestProperty("Cookie", cookie)
                Log.d("Cookie", cookie)
            }

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {

                val length = connection.contentLength

                Log.d("content length", length.toString() + "")

                if (length > 0) {
                    val temp = ByteArray(length)

                    val `is` = connection.inputStream
                    val folder = File(filePath)
                    if (!folder.exists()) {
                        folder.mkdir()
                    }

                    val file = File(filePath + fileName)

                    var read: Int

                    val fos = FileOutputStream(file)

                    while (true) {
                        read = `is`.read(temp)
                        if (read <= 0) {
                            break
                        }
                        fos.write(temp, 0, read)
                    }

                    fos.flush()
                    fos.close()
                    `is`.close()
                } else {
                    return null
                }

                connection.disconnect()

            }

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return filePath + fileName

    }

    override fun onPostExecute(s: String) {
        super.onPostExecute(s)
        onFinish(s)
    }
}
