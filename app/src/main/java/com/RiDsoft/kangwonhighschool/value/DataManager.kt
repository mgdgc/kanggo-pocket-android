package com.RiDsoft.kangwonhighschool.value

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.util.Log
import com.RiDsoft.kangwonhighschool.ui.schedule.teacher.TeacherScheduleObject
import com.google.gson.Gson
import com.google.gson.GsonBuilder

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Date

/**
 * Created by RiD on 2017. 7. 25..
 */

class DataManager(private val context: Context) {

    val grade: Int
    val classNumber: Int

    val isConnectedToNetwork: Boolean
        get() {
            val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networks = connManager.activeNetworkInfo
            return networks != null && networks.isConnected
        }

    val isTeacher: Boolean
        get() = grade == 0 && classNumber == 0

    val scheduleOfDay: String
        get() {
            return if (isTeacher) {
                getTeacherSchedule()
            } else {
                val schedulePref = context.getSharedPreferences("scheduleData", 0)
                val key = "$grade-$classNumber-$weekCode"
                schedulePref.getString(key, "정보 없음")
            }
        }

    private val weekCode: Int
        get() {
            val calendar = Calendar.getInstance()
            var week = calendar.get(Calendar.DAY_OF_WEEK)
            if (week == 1 || week == 7) {
                week = 0
            } else {
                week -= 2
            }
            return week
        }

    val mealData: String
        get() {
            val mealPref = context.getSharedPreferences(Key.KEY_MEAL, 0)
            val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd(E)")

            return ("오늘: " + simpleDateFormat.format(Date()) +
                    "\n급식: " + mealPref.getString(weekNum.toString() + "date", "정보 없음")
                    + "\n\n" + mealPref.getString(weekNum.toString() + "lunch", "정보 없음"))
        }

    private val weekNum: Int
        get() {
            val calendar = Calendar.getInstance()
            var i = 1
            val week = calendar.get(Calendar.DAY_OF_WEEK)
            when (week) {
                2 -> i = 1
                3 -> i = 2
                4 -> i = 3
                5 -> i = 4
                6 -> i = 5
                7 -> i = 1
                1 -> i = 1
            }
            return i
        }

    init {
        val userPref = context.getSharedPreferences(Key.KEY_USER, 0)
        grade = userPref.getInt(Key.KEY_USER_GRADE, 1)
        classNumber = userPref.getInt(Key.KEY_USER_CLASS, 1)
    }

    fun getScheduleOfWeek(grade: Int, classNum: Int): Array<String> {
        val schedulePref = context.getSharedPreferences("scheduleData", 0)
        val data = ArrayList<String>()
        for (i in 0..4) {
            val key = "$grade-$classNum-$i"
            data.add(schedulePref.getString(key, "정보 없음"))
        }
        return data.toTypedArray()
    }

    val classNumbers: IntArray
        get() {
            val schedulePref = context.getSharedPreferences("scheduleData", 0)
            return intArrayOf(schedulePref.getInt("firstClassNum", 1),
                    schedulePref.getInt("secondClassNum", 1),
                    schedulePref.getInt("thirdClassNum", 1))
        }

    val scheduleVersion: String
        get() {
            return context.getSharedPreferences("scheduleData", 0).getString("version", "")
        }

    fun getTeacherSchedule(position: Int): Array<TeacherScheduleObject> {
        val teacherPref = context.getSharedPreferences(Key.KEY_TEACHER, 0)

        val schedules: ArrayList<TeacherScheduleObject> = ArrayList()

        for (i in 0 until 8) {
            val scheduleString = teacherPref.getString("schedule_${position}_$i", null)

            if (scheduleString != null) {
                val schedule = Gson().fromJson(scheduleString, TeacherScheduleObject::class.java)
                schedules.add(schedule)
            } else {
                Log.d("DataManager", "schedule_${position}_$i is null")
                schedules.add(TeacherScheduleObject(position, i, "", "", ""))
            }
        }
        return schedules.toTypedArray()
    }

    private fun getTeacherSchedule(): String {
        val teacherSchedules = getTeacherSchedule(weekCode)
        val builder = StringBuilder()
        var i = 0
        for (s in teacherSchedules) {
            builder.append("${s.period + 1}교시: ")

            if (s.subject.trim() == "") {
                builder.append("없음")
            } else {
                builder.append(s.subject)
            }

            if (s.classNum.trim() != "") {
                builder.append(" (${s.classNum})")
            }

            if (i >= teacherSchedules.size) {
                continue
            }
            i++
            builder.append("\n\n")
        }
        return builder.toString()
    }

    fun saveTeacherSchedule(schedule: TeacherScheduleObject, period: Int, dayOfWeek: Int) {
        val teacherPref = context.getSharedPreferences(Key.KEY_TEACHER, 0)
        val teacherPrefEdit = teacherPref.edit()

        val gson = GsonBuilder().create()
        print("DataManager")
        teacherPrefEdit.putString("schedule_${dayOfWeek}_$period", gson.toJson(schedule))
        teacherPrefEdit.apply()
    }

    fun getPassCode(): String {
        val pwPref = context.getSharedPreferences("password", 0)
        return pwPref.getString("pass_code", "")
    }

    fun setPassCode(code: String) {
        val pwPref = context.getSharedPreferences("password", 0)
        val pwPrefEdit = pwPref.edit()
        pwPrefEdit.putString("pass_code", code)
        pwPrefEdit.apply()
    }
}
