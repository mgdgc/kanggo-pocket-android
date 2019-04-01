package com.RiDsoft.kangwonhighschool.ui.preview

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.ui.main.MainActivity

class InitializeActivity : AppCompatActivity() {

    private lateinit var initializePref: SharedPreferences

    companion object {
        const val KEY_INITIALIZE = "initialize"
        const val BOOL_NOTIFICATION_CHANNEL = "bool_notification_channel"

        const val CHANNEL_NOTIFICATION = "channel_notification"
        const val CHANNEL_EMERGENCE = "channel_emergence"
        const val CHANNEL_DAILY = "channel_daily"
        const val CHANNEL_SCHEDULE_UPDATE = "channel_schedule_update"
        const val CHANNEL_CLASS_ANNOUNCE = "channel_class_announce"
        const val CHANNEL_GRADE_ANNOUNCE = "channel_grade_announce"
        const val CHANNEL_MEMO = "channel_memo"
        const val CHANNEL_AIRTOOL = "channel_airtool"
        const val CHANNEL_KP_ASSIST = "channel_kp_assistant"

        const val GROUP_NOTIFICATION = "group_notification"
        const val GROUP_KP = "group_kanggo_pocket"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initialize)

        initializePref = getSharedPreferences(KEY_INITIALIZE, 0)

        val handler = Handler()
        handler.postDelayed(InitializeWork(), 300)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initializeNotificationChannel() {

        val notificationGroup = NotificationChannelGroup(GROUP_NOTIFICATION, getString(R.string.group_notification))
        val kpGroup = NotificationChannelGroup(GROUP_KP, getString(R.string.group_kanggo_pocket))

        val memo = NotificationChannel(
                CHANNEL_MEMO,
                getString(R.string.channel_memo),
                NotificationManager.IMPORTANCE_LOW
        )
        memo.description = getString(R.string.channel_memo_description)
        memo.enableLights(false)
        memo.lightColor = getColor(R.color.colorAccent)
        memo.enableVibration(false)
        memo.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        memo.group = GROUP_KP

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannelGroups(
                mutableListOf(notificationGroup, kpGroup)
        )
        notificationManager.createNotificationChannels(mutableListOf(memo))

        val deletedChannels = arrayListOf(
                CHANNEL_NOTIFICATION,
                CHANNEL_EMERGENCE,
                CHANNEL_DAILY,
                CHANNEL_SCHEDULE_UPDATE,
                CHANNEL_CLASS_ANNOUNCE,
                CHANNEL_GRADE_ANNOUNCE,
                CHANNEL_AIRTOOL,
                CHANNEL_KP_ASSIST)

        notificationManager.notificationChannelGroups.forEach { action ->
            for (i in 0 until deletedChannels.size) {
                action.channels.forEach {
                    if (it.id == deletedChannels[i]) {
                        notificationManager.deleteNotificationChannel(deletedChannels[i])
                    }
                }
            }
        }

    }

    override fun onBackPressed() {
        // 뒤로가기를 눌러도 동작하지 않도록 합니다.
    }

    fun onInitializeFinished() {
        val editor = initializePref.edit()
        editor.putBoolean(BOOL_NOTIFICATION_CHANNEL, true)
        editor.apply()
        startActivity(Intent(this@InitializeActivity, MainActivity::class.java))
        this.finish()
    }

    inner class InitializeWork : Runnable {
        override fun run() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                initializeNotificationChannel()
            }
            onInitializeFinished()
        }
    }

}