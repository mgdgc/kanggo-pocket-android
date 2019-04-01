package com.RiDsoft.kangwonhighschool.ui.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.preference.PreferenceManager
import android.widget.RemoteViews

import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.ui.schedule.student.ScheduleActivity
import com.RiDsoft.kangwonhighschool.ui.schedule.teacher.TeacherScheduleActivity
import com.RiDsoft.kangwonhighschool.value.DataManager

import java.util.Calendar

/**
 * Implementation of App Widget functionality.
 */
class ScheduleAppWidget : AppWidgetProvider() {

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

            val views = RemoteViews(context.packageName, R.layout.widget_shcedule)

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

            views.setTextViewText(R.id.schedule_1, result)

            if (getter.isTeacher) {
                val intent = Intent(context, TeacherScheduleActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
                views.setOnClickPendingIntent(R.id.view_schedule_widget, pendingIntent)
            } else {
                val intent = Intent(context, ScheduleActivity::class.java)
                val pendingIntent = PendingIntent.getActivities(context, 0, arrayOf(intent), 0)
                views.setOnClickPendingIntent(R.id.view_schedule_widget, pendingIntent)
            }

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        internal val week: String?
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
    }
}

