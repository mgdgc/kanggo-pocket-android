package com.RiDsoft.kangwonhighschool.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager

import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.ui.main.MainActivity
import com.RiDsoft.kangwonhighschool.ui.preview.InitializeActivity
import com.RiDsoft.kangwonhighschool.ui.preview.PreviewActivity
import com.RiDsoft.kangwonhighschool.value.Key
import com.RiDsoft.kangwonhighschool.worker.MarketVersionChecker
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging

/**
 * Created by RiD on 2016. 1. 18..
 */
class StartingActivity : AppCompatActivity() {

    private lateinit var appPref: SharedPreferences
    private lateinit var userPref: SharedPreferences
    private lateinit var appPrefEdit: SharedPreferences.Editor


    private val isConnected: Boolean
        get() {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return if (networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_WIFI) {
                true
            } else {
                true
            }
        }

    private val versionCode: Int
        get() {
            return try {
                packageManager.getPackageInfo(packageName, 0).versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                0
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starting)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        userPref = getSharedPreferences(Key.KEY_USER, 0)
        appPref = getSharedPreferences(Key.KEY_APP, 0)
        appPrefEdit = appPref.edit()

        if (appPref.getBoolean("first", true)) {
            startActivity(Intent(this@StartingActivity, PreviewActivity::class.java))
            finish()
        } else {
            if (isConnected) {
                val handler = Handler()
                handler.postDelayed(VersionCheckAction(), 500)
            } else {
                val handler = Handler()
                handler.postDelayed(StartAction(), 1000)
            }

        }

    }

    private inner class VersionCheckAction : Runnable {
        override fun run() {
            val task = VersionCheckTask()
            task.execute(packageName)
        }
    }

    private inner class StartAction : Runnable {
        override fun run() {
            afterTask()
        }
    }

    private fun updateRequired(current: String?, latest: String?) {
        val builder1 = AlertDialog.Builder(this@StartingActivity)
        builder1.setTitle("업데이트가 있습니다.")
                .setMessage("새 버전이 확인되었습니다. " +
                        "\n\n현재 버전: v" + current +
                        "\n최신 버전: v" + latest +
                        "\n\n앱을 업데이트 해주시기 바랍니다.")
                .setPositiveButton("업데이트") { dialog, which ->
                    dialog.dismiss()
                    val marketIntent = Intent(Intent.ACTION_VIEW)
                    marketIntent.data = Uri.parse("market://details?id=$packageName")
                    startActivity(marketIntent)
                    this@StartingActivity.finish()
                }
                .setNegativeButton("나중에") { dialog, which ->
                    dialog.dismiss()
                    afterTask()
                }
                .show()
    }

    private fun afterTask() {
        if (appPref.getInt("updated", 0) != versionCode) {
            //어플 업데이트 시
            var updateLog = ""
            var i = 1
            for (s in resources.getStringArray(R.array.array_change_log)) {
                updateLog += "$i. $s\n"
                i++
            }
            val builder = AlertDialog.Builder(this@StartingActivity)
            builder.setTitle(getString(R.string.update_lists))
                    .setMessage(updateLog)
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.string_confirm)) { dialogInterface, i ->
                        startActivity(Intent(this@StartingActivity, InitializeActivity::class.java))
                        appPrefEdit.putInt("updated", versionCode)
                        appPrefEdit.apply()
                        dialogInterface.dismiss()
                        finish()
                    }
                    .show()
        } else {
            //어플 업데이트가 없을 시
            startActivity(Intent(this@StartingActivity, MainActivity::class.java))
            overridePendingTransition(R.anim.anim_slide_to_right, R.anim.anim_slide_to_left)
            finish()
        }
    }

    private inner class VersionCheckTask : AsyncTask<String, Int, Boolean>() {

        internal var marketVersion: String? = null
        internal var deviceVersion: String? = null

        override fun doInBackground(vararg params: String): Boolean? {
            return isUpdateExist(params[0])
        }

        private fun isUpdateExist(packageName: String): Boolean {
            marketVersion = MarketVersionChecker.getMarketVersion(packageName)
            deviceVersion = "0"

            try {
                deviceVersion = packageManager.getPackageInfo(getPackageName(), 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return if (marketVersion != null && deviceVersion != null) {

                marketVersion!! > deviceVersion!!

            } else {
                false
            }
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            if (result!!) {
                updateRequired(deviceVersion, marketVersion)
            } else {
                afterTask()
            }
        }
    }

    override fun onBackPressed() {

    }
}
