package com.RiDsoft.kangwonhighschool.ui.web.worker

import android.os.AsyncTask

/**
 * Created by RiD on 2017. 7. 9..
 */

abstract class WebTask : AsyncTask<String, Int, Int>() {

    var webValueObjects: Array<WebValueObject>? = null

    abstract fun onPreProceed()

    abstract fun onUpdate(progress: Int)

    abstract fun onFinish(vararg result: Int)

    override fun onPreExecute() {
        super.onPreExecute()
        onPreProceed()
    }

    override fun doInBackground(vararg params: String): Int? {

        val url = params[0]

        val parser = WebParser()

        parser.setCookie(params[0])

        val result = parser.doParse(url)

        return if (result == "!HttpURLConnection.HTTP_OK") {
            RESULT_FAIL_404
        } else if (result == "URLException") {
            RESULT_FAIL_URLEXCEPTION
        } else if (result == "IOException") {
            RESULT_FAIL_IOEXCEPTION
        } else {
            if (parser.isNoContent) {
                RESULT_LAST_CONTENT
            } else {
                webValueObjects = parser.parseHTML(result)
                RESULT_SUCCESS
            }
        }

    }

    override fun onPostExecute(integer: Int?) {
        super.onPostExecute(integer)
        if (integer != null) {
            onFinish(integer)
        }
    }

    companion object {
        const val RESULT_SUCCESS = 200 //파싱 성공
        const val RESULT_FAIL_404 = 404
        const val RESULT_FAIL_URLEXCEPTION = 400
        const val RESULT_FAIL_IOEXCEPTION = 400
        const val RESULT_LAST_CONTENT = 440
    }
}
