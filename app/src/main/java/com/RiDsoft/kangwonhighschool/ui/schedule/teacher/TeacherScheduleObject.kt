package com.RiDsoft.kangwonhighschool.ui.schedule.teacher

import java.time.DayOfWeek
import java.util.*


data class TeacherScheduleObject(var dayOfWeek: Int, var period: Int, var subject: String,
                                 var classNum: String, var memo: String)