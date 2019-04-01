package com.RiDsoft.kangwonhighschool.ui.schedule.student

import android.content.Context
import net.htmlparser.jericho.Element
import net.htmlparser.jericho.HTMLElementName
import net.htmlparser.jericho.Source
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.ArrayList
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.HttpsURLConnection

class NewScheduleParser(private val context: Context) {

    var scheduleUrl: String = ""

    constructor(context: Context, scheduleUrl: String): this(context) {
        this.scheduleUrl = scheduleUrl
    }

    fun getScheduleSource() : String? {
        val url = URL(scheduleUrl)
        val conn = url.openConnection() as HttpsURLConnection

        conn.useCaches = false
        conn.doOutput = true
        conn.connectTimeout = 5000

        conn.hostnameVerifier = HostnameVerifier { hostname, session ->
            hostname == url.host
        }

        if (conn.responseCode != HttpsURLConnection.HTTP_OK) {
            conn.disconnect()
            return null
        }

        val bufferedReader = BufferedReader(InputStreamReader(conn.inputStream))

        val stringBuilder = StringBuilder()
        while (true) {
            stringBuilder.append(bufferedReader.readLine() ?: break)
        }

        conn.disconnect()

        return stringBuilder.toString()
    }

    fun parseSource(string: String): Array<ScheduleObject>? {

        val source: Source

        try {
            source = Source(string)
            source.fullSequentialParse()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

        var elements = source.getAllElements(HTMLElementName.TABLE)

        var tbody: Element? = null
        for (e in elements) {
            e.getAttributeValue("class")?.let {
                if (it == "waffle") {
                    tbody = e.getFirstElement(HTMLElementName.TBODY)
                }
            }
        }

        tbody?.getAllElements(HTMLElementName.TR)?.let { trs ->
            var weekHeader: ArrayList<String> = ArrayList()
            var periodHeader: ArrayList<String> = ArrayList()

            val result: ArrayList<ScheduleObject> = ArrayList()

            for ((i, e) in trs.withIndex()) {
                val schedule: ScheduleObject = ScheduleObject()
                val tds = e.getAllElements(HTMLElementName.TD)
                when (i) {
                    0, 1 -> {
                        val array: ArrayList<String> = ArrayList()
                        for (t in tds) {
                            array.add(t.content.toString().trim())
                        }
                        if (i == 0) {
                            weekHeader = array
                        } else {
                            periodHeader = array
                        }
                    }

                    else -> {

                    }
                }
//                result.add(null)
            }
        }

        var weeks = elements

        return null
    }

}