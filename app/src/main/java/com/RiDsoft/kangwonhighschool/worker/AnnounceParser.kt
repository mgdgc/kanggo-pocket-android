package com.RiDsoft.kangwonhighschool.worker

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.RiDsoft.kangwonhighschool.ui.announce.AnnounceObject
import com.RiDsoft.kangwonhighschool.ui.announce.view.AttachObject
import com.google.gson.Gson
import net.htmlparser.jericho.HTMLElementName
import net.htmlparser.jericho.Source
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.ProtocolException
import java.net.URL
import java.util.ArrayList

class AnnounceParser {

    companion object {
        public final val URL_BASE = "http://45.32.22.143"
        public final val URL_ANNOUNCE_LIST = "http://45.32.22.143/announce/announce_json.jsp"
        public final val URL_ANNOUNCE = "http://45.32.22.143/announce/view_json.jsp"
        public final val URL_IMAGE_VIEW = "http://45.32.22.143/file/image_viewer.jsp"
        public final val URL_FILE = "http://45.32.22.143/file/file_info_json.jsp"
    }

    fun getDocument(urlStr: String): String? {
        try {
            val url = URL(urlStr)
            val conn = url.openConnection() as HttpURLConnection

            conn.requestMethod = "GET"
            conn.doInput = true
            conn.useCaches = false

            conn.setRequestProperty("Accept-Charset", "UTF-8")
            conn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;cahrset=UTF-8")

            if (conn.responseCode != HttpURLConnection.HTTP_OK) {
                Log.d(" HttpURLConnection", "Response code != HTTP_OK (200)")
                return null
            }

            val inputStream = conn.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream, "utf-8"))
            val builder = StringBuilder()

            var line: String?
            do {
                line = bufferedReader.readLine()
                builder.append(line ?: "")
                builder.append("\n")
            } while (line != null)

            return builder.toString()
        } catch (e: ProtocolException) {
            e.printStackTrace()
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    fun getAnnouncementList(document: String): ArrayList<AnnounceObject>? {
        val data: ArrayList<AnnounceObject> = ArrayList()

        val json = Gson().fromJson<Array<String>>(document, Array<String>::class.java) ?: return null

        for (s in json) {
            data.add(Gson().fromJson(s, AnnounceObject::class.java))
        }

        return data
    }

    fun getAnnouncement(document: String): AnnounceObject? {
        return Gson().fromJson<AnnounceObject>(document, AnnounceObject::class.java) ?: null
    }

    fun getImageById(id: Int): Bitmap? {
        var result: Bitmap? = null

        try {
            val document = getDocument("$URL_IMAGE_VIEW?fileId=$id") ?: return null
            val imageUrl = getImageURLFromDocument(document) ?: return null
            val url = URL(URL_BASE + imageUrl)
            val conn = url.openConnection() as HttpURLConnection

            conn.doInput = true
            conn.connect()

            val inputStream = conn.inputStream
            result = BitmapFactory.decodeStream(inputStream)

        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    private fun getImageURLFromDocument(document: String): String? {
        val source = Source(document)
        source.fullSequentialParse()

        val img = source.getFirstElement(HTMLElementName.IMG)

        return img.getAttributeValue("src")
    }

    fun getFileObjectById(id: Int): AttachObject? {
        val document = getDocument("$URL_FILE?fileId=$id") ?: return null
        return Gson().fromJson<AttachObject>(document, AttachObject::class.java)
    }

}