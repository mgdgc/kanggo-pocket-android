package com.RiDsoft.kangwonhighschool.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.widget.RemoteViews

import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.ui.meal.MealActivity
import com.RiDsoft.kangwonhighschool.ui.schedule.student.ScheduleActivity
import com.RiDsoft.kangwonhighschool.value.DataManager

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

/**
 * Implementation of App Widget functionality.
 */
class LGSmartBulletinWidget : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.widget_for_lg)

            val userPref: SharedPreferences = context.getSharedPreferences("user", 0)
            val getter = DataManager(context)


            //선택 1, 2, 3을 사용자가 설정한 것으로 변경
            val setting = PreferenceManager.getDefaultSharedPreferences(context)
            var s = getter.scheduleOfDay

            val result: String?
            for (i in 1..3) {
                val c = setting.getString("pref_pick$i", "선$i")!!.replace("0", "선$i")
                s = s.replace("선$i", c)
            }
            result = s

            views.setTextViewText(R.id.txt_widget_schedule, result)


            val intent = Intent(context, ScheduleActivity::class.java)
            intent.putExtra("type", if (userPref.getInt("grade", 1) == 1) 0 else 1)
            intent.putExtra("grade", userPref.getInt("grade", 1))
            intent.putExtra("class", userPref.getInt("class", 1))
            val pendingIntent = PendingIntent.getActivities(context, 0, arrayOf(intent), 0)
            views.setOnClickPendingIntent(R.id.card_widget_schedule, pendingIntent)

            val mealPref = context.getSharedPreferences("meal", 0)
            val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd(E)")
            views.setTextViewText(R.id.txt_widget_meal,
                    "오늘: " + simpleDateFormat.format(Date()) +
                            "\n급식: " + mealPref.getString(weekNum.toString() + "date", "정보 없음")
                            + "\n\n" + mealPref.getString(weekNum.toString() + "lunch", "정보 없음"))
            val intent2 = Intent(context, MealActivity::class.java)
            val pendingIntent2 = PendingIntent.getActivities(context, 0, arrayOf(intent2), 0)
            views.setOnClickPendingIntent(R.id.card_widget_meal, pendingIntent2)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private val week: String?
            get() {
                val calendar = Calendar.getInstance()
                var s: String? = null
                val week = calendar.get(Calendar.DAY_OF_WEEK)
                if (week == 2)
                    s = "mon"
                else if (week == 3)
                    s = "tues"
                else if (week == 4)
                    s = "wed"
                else if (week == 5)
                    s = "thurs"
                else if (week == 6)
                    s = "fri"
                else if (week == 7)
                    s = "mon"
                else if (week == 1) s = "mon"
                return s
            }

        private val weekNum: Int
            get() {
                val calendar = Calendar.getInstance()
                var i = 1
                val week = calendar.get(Calendar.DAY_OF_WEEK)
                if (week == 2)
                    i = 1
                else if (week == 3)
                    i = 2
                else if (week == 4)
                    i = 3
                else if (week == 5)
                    i = 4
                else if (week == 6)
                    i = 5
                else if (week == 7)
                    i = 1
                else if (week == 1) i = 1
                return i
            }
    }
}

