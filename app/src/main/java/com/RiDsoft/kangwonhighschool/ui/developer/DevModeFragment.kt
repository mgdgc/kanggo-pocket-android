package com.RiDsoft.kangwonhighschool.ui.developer

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.widget.Toast

import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.ui.new_schedule.NewScheduleActivity
import com.google.firebase.iid.FirebaseInstanceId

/**
 * Created by RiD on 2017. 10. 20..
 */

class DevModeFragment : PreferenceFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference_dev_mode)

        val token = FirebaseInstanceId.getInstance().token
        findPreference("pref_fcm_token").summary = token
        findPreference("pref_fcm_token").onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val clipboardManager = activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(token, token)
            clipboardManager.primaryClip = clipData
            Toast.makeText(activity, token, Toast.LENGTH_LONG).show()
            false
        }

        findPreference("dev_new_schedule").setOnPreferenceClickListener {
            startActivity(Intent(activity, NewScheduleActivity::class.java))
            true
        }
    }

    companion object {

        const val PREF_GRADUATE_MODE = "pref_graduate"
    }
}
