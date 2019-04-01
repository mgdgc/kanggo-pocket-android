package com.RiDsoft.kangwonhighschool.ui.web.worker

import android.os.Build
import android.util.Log
import android.webkit.CookieManager
import android.webkit.ValueCallback

import net.htmlparser.jericho.Element
import net.htmlparser.jericho.HTMLElementName
import net.htmlparser.jericho.Source

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpCookie
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.ArrayList

/**
 * Created by RiD on 2017. 7. 9..
 */

class WebParser {

    private var URL_WEB = ""
    var isNoContent = false

    fun doParse(s: String): String {
        URL_WEB = s

        try {

            val url = URL(URL_WEB)
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

            connection.connect()

            var data = ""
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val bufferedReader = BufferedReader(
                        InputStreamReader(connection.inputStream)
                )

                while (true) {
                    val line = bufferedReader.readLine() ?: break
                    data += line
                }

                bufferedReader.close()
            } else {
                return "!HttpURLConnection.HTTP_OK"
            }

            connection.disconnect()

            return data

        } catch (e: MalformedURLException) {
            e.printStackTrace()
            return "URLException"
        } catch (e: IOException) {
            e.printStackTrace()
            return "IOException"
        }

    }

    fun parseHTML(html: String): Array<WebValueObject>? {

        val source = Source(html)

        source.fullSequentialParse()
        val table = source.getAllElements(HTMLElementName.TABLE)

        if (table != null) {

            var elementNum = 0
            for (i in table.indices) {
                if ((table[i] as Element).getAttributeValue("class") == "wb") {
                    elementNum = i
                    break
                }
            }

            if (table.size > elementNum) {

                val elementTable = table[elementNum] as Element
                val tbody = elementTable.getAllElements(HTMLElementName.TBODY)[0]
                val tr = tbody.getAllElements(HTMLElementName.TR)

                val arrNVO = ArrayList<WebValueObject>()
                for (i in tr!!.indices) {
                    val nvo = WebValueObject()
                    if (tr.size > 1) {

                        try {
                            val td = tr[i].getAllElements(HTMLElementName.TD)[1]
                            val a = td.getAllElements(HTMLElementName.A)[0]
                            nvo.titleText = a.content.toString()
                            nvo.writer = tr[i].getAllElements(HTMLElementName.TD)[2].content.toString()
                            nvo.viewCount = tr[i].getAllElements(HTMLElementName.TD)[3].content.toString()
                            nvo.date = tr[i].getAllElements(HTMLElementName.TD)[4].content.toString()
                            val goViewInfo = a.getAttributeValue("onClick").split("'".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                            val urlInfo = Integer.valueOf(goViewInfo[3].trim({ it <= ' ' }))!!
                            nvo.urlInfo = urlInfo

                            arrNVO.add(nvo)

                            isNoContent = false

                        } catch (e: Exception) {
                            return null
                        }

                    } else {
                        isNoContent = true
                        return null
                    }
                }

                return arrNVO.toTypedArray<WebValueObject>()

            } else {
                return null
            }
        } else {
            return null
        }
    }

    fun setCookie(cookieUrl: String) {

        try {
            val url = URL(cookieUrl)
            val connection = url.openConnection() as HttpURLConnection

            val headerFields = connection.headerFields
            val cookiesHeader = headerFields["Set-Cookie"]

            if (cookiesHeader != null) {
                for (cookie in cookiesHeader) {
                    val cookieName = HttpCookie.parse(cookie)[0].name
                    val cookieValue = HttpCookie.parse(cookie)[0].value

                    val cookieString = cookieName + "=" + cookieValue

                    CookieManager.getInstance().setCookie("http://kanggo.net", cookieString)

                }
            }


        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun clearCookie() {
        val cookieManager = CookieManager.getInstance()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.removeSessionCookies { }
        } else {
            cookieManager.removeSessionCookie()
        }
    }

}
