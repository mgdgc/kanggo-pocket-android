package com.RiDsoft.kangwonhighschool.ui.user.settings

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceManager
import android.preference.SwitchPreference
import android.support.v7.app.AlertDialog
import android.widget.Toast

import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.ui.developer.DevModeActivity
import com.RiDsoft.kangwonhighschool.ui.secure.LogInActivity
import com.RiDsoft.kangwonhighschool.ui.user.EditUserInfoActivity
import com.RiDsoft.kangwonhighschool.value.DataManager
import com.RiDsoft.kangwonhighschool.value.Key
import com.github.ajalt.reprint.core.Reprint

/**
 * Created by soc06_000 on 2015-03-06.
 */
class SettingFragment : PreferenceFragment() {
    private var easterEggCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preference_setting)

    }

    override fun onResume() {
        super.onResume()

        val userPref = activity.getSharedPreferences("user", 0)
        val grade: String
        val classN: String
        val number: String
        val name: String?

        grade = Integer.toString(userPref.getInt("grade", 0))
        classN = Integer.toString(userPref.getInt("class", 0))
        number = if (userPref.getInt("number", 0) < 10) {
            "0" + userPref.getInt("number", 0)
        } else {
            Integer.toString(userPref.getInt("number", 0))
        }
        name = userPref.getString("name", "보호자")

        val mStudentPref = findPreference("pref_student")
        val studentInfo = if (userPref.getInt("grade", 0) == 0 && userPref.getInt("class", 0) == 0) {
            "$name ${getString(R.string.teacher)}"
        } else {
            "$grade$classN$number, $name"
        }

        mStudentPref.summary = studentInfo
        mStudentPref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            startActivity(Intent(activity, EditUserInfoActivity::class.java))
            true
        }

        val mChaneLogPref = findPreference("pref_change_log")
        mChaneLogPref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            var changeLog = ""
            var i = 1
            for (s in resources.getStringArray(R.array.array_change_log)) {
                changeLog += "$i. $s\n\n"
                i++
            }
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("변경사항")
                    .setMessage(changeLog)
                    .setCancelable(true)
                    .setPositiveButton("닫기") { dialogInterface, i -> dialogInterface.dismiss() }
                    .show()
            true
        }

        val mVersionPref = findPreference("pref_version")
                
        var version = "1.0"
        var versionCode = 0
        try {
            val i = activity.packageManager.getPackageInfo(activity.packageName, 0)
            version = i.versionName
            versionCode = i.versionCode
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mVersionPref.summary = "$version.$versionCode"

        val pick1: Preference = findPreference("pref_pick1")
        val pick2: Preference = findPreference("pref_pick2")
        val pick3: Preference = findPreference("pref_pick3")

        val appPref = PreferenceManager.getDefaultSharedPreferences(activity)
        pick1.summary = appPref.getString("pref_pick1", "없음")
        pick2.summary = appPref.getString("pref_pick2", "없음")
        pick3.summary = appPref.getString("pref_pick3", "없음")

        pick1.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, o ->
            pick1.summary = o.toString()
            true
        }

        pick2.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, o ->
            pick2.summary = o.toString()
            true
        }

        pick3.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { preference, o ->
            pick3.summary = o.toString()
            true
        }

        val mKakaoTalk = findPreference("kakaotalk")
        mKakaoTalk.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_uaxkxaC"))
            startActivity(intent)
            true
        }

        (findPreference("pref_app_lock") as SwitchPreference).setOnPreferenceChangeListener { preference, newValue ->
            if (newValue as Boolean) {
                val intent = Intent(activity, LogInActivity::class.java)
                intent.putExtra("mode", 1)
                startActivity(intent)
            } else {
                val manager = DataManager(activity)
                manager.setPassCode("")
            }

            true
        }

        findPreference("pref_reset_pass_code").setOnPreferenceClickListener {
            val intent = Intent(activity, LogInActivity::class.java)
            intent.putExtra("mode", 2)
            startActivity(intent)
            true
        }

        Reprint.initialize(activity)
        val fingerprintPref = findPreference("pref_use_fingerprint")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            fingerprintPref.isEnabled = false
        } else {
            fingerprintPref.isEnabled = Reprint.isHardwarePresent()
        }

    }
}
