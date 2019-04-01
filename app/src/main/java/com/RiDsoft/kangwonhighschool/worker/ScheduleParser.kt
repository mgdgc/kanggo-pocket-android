package com.RiDsoft.kangwonhighschool.worker

import android.content.Context
import android.util.Log

import net.htmlparser.jericho.Element
import net.htmlparser.jericho.HTMLElementName
import net.htmlparser.jericho.Segment
import net.htmlparser.jericho.Source
import java.io.BufferedReader

import java.io.IOException
import java.io.InputStreamReader
import java.lang.NumberFormatException
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList
import javax.net.ssl.HttpsURLConnection

/**
 * Created by RiD on 2017. 2. 28..
 */

class ScheduleParser(private val context: Context) {

    var classNumData: IntArray = IntArray(3)

    fun getScheduleTable(): Array<ScheduleModel>? {
        val source: Source
        try {
            source = Source(URL(newURL))
            source.fullSequentialParse()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        var table: Element? = null
        for (e in source.getAllElements(HTMLElementName.TABLE)) {
            if (e.getAttributeValue("class") == "waffle")
                table = e
        }
        if (table == null) return null

        val tbody = table.getFirstElement(HTMLElementName.TBODY)
        val tr = tbody.getAllElements(HTMLElementName.TR)
        val td = tbody.getAllElements(HTMLElementName.TD)
        val scheduleOfWeek = tr[1].getAllElements(HTMLElementName.TD)

        val schematic = intArrayOf(0, 1, 1, 1, 1)
        var prev = 0
        var indexOfSchematic = 0
        for (i in scheduleOfWeek.indices) {
            val curr = Integer.parseInt(scheduleOfWeek[i].content.textExtractor.toString())
            if (prev < curr) {
                prev = curr
                schematic[indexOfSchematic]++
            } else {
                prev = 0
                indexOfSchematic++
            }
        }

        var startIndex = 0
        for (i in td.indices) {
            if (td[i].content.textExtractor.toString() == "1-1") {
                startIndex = i
                break
            }
        }

        val processedTd = ArrayList<Element>()
        for (i in startIndex until td.size) {
            processedTd.add(td[i])
        }
        val scheduleData = ArrayList<String>()
        val teacherData = ArrayList<String>()

        for (i in 0..2) {
            classNumData[i] = 0
        }
        var totalCountOfSchedule = 1
        var totalCountOfTeacher = 0
        for (i in schematic) {
            totalCountOfSchedule += i
            totalCountOfTeacher += i
        }
        var count = 0
        var mode = 0
        for (e in processedTd) {
            count++
            if (e.content.textExtractor.toString().contains("-")) {
                val scheduleInfo = e.content.textExtractor.toString().split("-".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                when (scheduleInfo[0]) {
                    "1" -> classNumData[0]++
                    "2" -> classNumData[1]++
                    "3" -> classNumData[2]++
                    else -> {
                    }
                }
                mode = 0
            } else {
                if (count <= totalCountOfSchedule) {
                    if (mode == 0) {
                        scheduleData.add(e.content.textExtractor.toString())
                        if (count >= totalCountOfSchedule) {
                            mode = 1
                            count = 0
                        }
                    } else {
                        teacherData.add(e.content.textExtractor.toString())
                        if (count >= totalCountOfTeacher) {
                            mode = 0
                            count = 0
                        }
                    }
                }
            }
        }

        val data = ArrayList<ScheduleModel>()
        var index = 0
        for (i in 0..2) {
            for (j in 0 until classNumData[i]) {
                for (k in 0..4) {
                    val schedule = StringBuilder()
                    for (l in 1..schematic[k]) {
                        if (l != 1) {
                            schedule.append("\n\n")
                        }
                        val scheduleString = scheduleData[index].trim { it <= ' ' }
                        val teacherString = teacherData[index].trim { it <= ' ' }
                        val s = l.toString() + "교시: "
                        val s1 = if (teacherString == "") "없음" else "$scheduleString ($teacherString)"
                        schedule.append(s).append(s1)
                        index++
                    }
                    Log.d((i + 1).toString() + (j + 1).toString() + k.toString(), schedule.toString())
                    data.add(ScheduleModel(i + 1, j + 1, k, schedule.toString()))
                }
            }
        }

        return data.toTypedArray()
    }

    val version: String
        get() {
            val source = Source(URL(newURL))
            return try {
                source.fullSequentialParse()
                val head: List<Element> = source.getAllElements(HTMLElementName.HEAD)
                head[0].getFirstElement(HTMLElementName.TITLE).content.textExtractor.toString()
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }

    internal fun saveScheduleData(scheduleVersion: String, data: Array<ScheduleModel>, classNums: IntArray): Int {
        val schedulePref = context.getSharedPreferences("scheduleData", 0)
        val editor = schedulePref.edit()
        for (s in data) {
            val key = s.grade.toString() + "-" + s.classNum + "-" + s.dayOfWeek
            editor.putString(key, s.schedule)
        }
        editor.putInt("firstClassNum", classNums[0])
        editor.putInt("secondClassNum", classNums[1])
        editor.putInt("thirdClassNum", classNums[2])
        editor.putString("version", scheduleVersion)
        editor.apply()
        return 0
    }

    companion object {
        private const val newURL = "https://docs.google.com/spreadsheets/d/e/2PACX-1vTwqkcmlYXVK7vk9CFlzybOt3rGMvH4nZl-F6_17J0sOpupucyVNCmS9vpogyyJDkm8ePbV3-hvKpqg/pubhtml"
    }
}

class ScheduleModel(var grade: Int, var classNum: Int, var dayOfWeek: Int //0: 월요일, 4: 금요일, ...
                             , var schedule: String)
