package com.RiDsoft.kangwonhighschool.ui.web.worker

import android.graphics.Bitmap
import android.os.AsyncTask

import java.util.ArrayList

/**
 * Created by RiD on 2017. 7. 10..
 */

abstract class WebDocTask : AsyncTask<String, Int, Int>() {

    var title: String? = null
    var content: String? = null
    var writer: String? = null
    var date: String? = null
    var attach: List<String>? = null
    var attachLink: List<String>? = null
    var imgUrls: List<String>? = null
    var bitmaps: List<Bitmap> = ArrayList()

    abstract fun onPreProceed()

    abstract fun onUpdate(progress: Int)

    abstract fun onFinish(result: Int)

    override fun onPreExecute() {
        super.onPreExecute()
        onPreProceed()
    }

    override fun doInBackground(vararg strings: String): Int? {

        val url = strings[0]

        val webParser = WebParser()
        val html = webParser.doParse(url)

        val parser = WebDocParser()
        val result = parser.doParse(html)

        if (result == 0) {

            title = parser.title
            content = parser.content
            writer = parser.writer
            date = parser.date
            attach = parser.attachment
            attachLink = parser.attachLink

            return 0

        } else {
            return 1

        }
    }

    override fun onPostExecute(integer: Int?) {
        super.onPostExecute(integer)
        onFinish(integer!!)
    }

}
