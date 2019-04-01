package com.RiDsoft.kangwonhighschool.ui.schedule.student

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.ui.main.features.MapActivity
import com.RiDsoft.kangwonhighschool.ui.schedule.ScheduleDownloadActivity
import com.RiDsoft.kangwonhighschool.ui.user.settings.SettingFragment
import com.RiDsoft.kangwonhighschool.value.DataManager
import com.RiDsoft.kangwonhighschool.value.Key

import java.util.Calendar

/**
 * Created by RiD on 2016. 10. 19..
 */

class ScheduleActivity : AppCompatActivity() {

    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private var mTxtToday: TextView? = null

    private var mUserPref: SharedPreferences? = null

    private var mType: Int = 0
    private var mGrade: Int = 0
    private var mClassN: Int = 0

    private val isConnected: Boolean
        get() {
            val isConnected: Boolean?
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            isConnected = networkWifi.isConnected
            return isConnected
        }

    private val defaultGrade: Int
        get() = mUserPref!!.getInt(Key.KEY_USER_GRADE, 1)

    private val defaultClassN: Int
        get() = mUserPref!!.getInt(Key.KEY_USER_CLASS, 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)
        val toolbar = findViewById<View>(R.id.toolbar_schedule) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val appPref = getSharedPreferences("app", 0)
        val settingPref = PreferenceManager.getDefaultSharedPreferences(this)
        mUserPref = getSharedPreferences(Key.KEY_USER, 0)

        supportActionBar!!.title = DataManager(this).scheduleVersion

        getIntentData()

        mTxtToday = findViewById(R.id.txt_schedule_today)

        mRecyclerView = findViewById(R.id.recycler_schedule)
        val layoutManager = GridLayoutManager(
                this, if (settingPref.getBoolean("pref_low_dpi", false)) 1 else 2)
        mRecyclerView!!.layoutManager = layoutManager
        mRecyclerView!!.setHasFixedSize(true)

        mAdapter = ScheduleAdapter(this, mType, mGrade, mClassN, object : ScheduleAdapter.UICallBack {
            override fun onItemClick(position: Int) {
                val appPref = PreferenceManager.getDefaultSharedPreferences(this@ScheduleActivity)
                val builder = AlertDialog.Builder(this@ScheduleActivity)
                builder.setTitle("선택교과")
                        .setMessage(
                                "선택1: " + appPref.getString("pref_pick1", "없음")
                                        + "\n\n선택2: " + appPref.getString("pref_pick2", "없음")
                                        + "\n\n선택3: " + appPref.getString("pref_pick3", "없음")
                        )
                        .setCancelable(true)
                        .setPositiveButton("닫기") { dialogInterface, i -> dialogInterface.dismiss() }
                        .setNeutralButton("수정") { dialogInterface, i ->
                            dialogInterface.dismiss()
                            startActivity(Intent(this@ScheduleActivity, SettingFragment::class.java))
                        }
                        .show()
            }

            override fun onItemLongClick(position: Int, content: String) {
                copyContent(content)
            }
        })
        mRecyclerView!!.adapter = mAdapter

        val mFAB = findViewById<FloatingActionButton>(R.id.btn_map)
        mFAB.setOnClickListener { startActivity(Intent(this@ScheduleActivity, MapActivity::class.java)) }

        setTodaysWeek()

    }

    private fun getIntentData() {
        val intent = intent
        mType = intent.getIntExtra("type", 0)
        mGrade = intent.getIntExtra("grade", defaultGrade)
        mClassN = intent.getIntExtra("class", defaultClassN)
    }

    private fun copyContent(content: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(content, content)
        clipboardManager.primaryClip = clipData
        Toast.makeText(this, "클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun setTodaysWeek() {
        mTxtToday!!.text = mGrade.toString() + "학년 " + mClassN + "반, " + dayKor
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_schedule, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> this@ScheduleActivity.finish()

            R.id.action_update -> {
                startActivity(Intent(this@ScheduleActivity, ScheduleDownloadActivity::class.java))
                this@ScheduleActivity.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {

        val KEY_GRADE = "grade"
        val KEY_CLASS_NUMBER = "class"

        val dayKor: String
            get() {
                val cal = Calendar.getInstance()
                val week = arrayOf("일", "월", "화", "수", "목", "금", "토")
                val cnt = cal.get(Calendar.DAY_OF_WEEK) - 1
                return week[cnt] + "요일"
            }
    }

}
