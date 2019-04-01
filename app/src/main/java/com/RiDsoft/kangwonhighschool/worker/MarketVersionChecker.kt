package com.RiDsoft.kangwonhighschool.worker

import android.util.Log

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

/**
 * Created by RiD on 2017. 6. 9..
 */

object MarketVersionChecker {

    fun getMarketVersion(packageName: String): String? {

        var data = ""
        var version: String?

        try {
            val url = URL("https://play.google.com/store/apps/details?id=$packageName")

            val connection = url.openConnection() as HttpURLConnection

            connection.connectTimeout = 5000
            connection.useCaches = false
            connection.doOutput = true

            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val bufferedReader = BufferedReader(
                        InputStreamReader(connection.inputStream)
                )

                while (true) {
                    val line = bufferedReader.readLine() ?: break
                    data += line
                }

                bufferedReader.close()
            }

            connection.disconnect()

        } catch (e: MalformedURLException) {
            e.printStackTrace()
            Log.d("version", "MalformedURLException")
            return null
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("version", "IOException")
            return null
        }

        val startToken = "softwareVersion\">"
        val endToken = "<"

        val index = data.indexOf(startToken)

        if (index == -1) {
            version = null
            Log.d("version", "null")
        } else {
            version = data.substring(index + startToken.length, index + startToken.length + 100)
            version = version!!.substring(0, version.indexOf(endToken)).trim({ it <= ' ' })
            Log.d("version", version)
        }
        return version
    }
}
