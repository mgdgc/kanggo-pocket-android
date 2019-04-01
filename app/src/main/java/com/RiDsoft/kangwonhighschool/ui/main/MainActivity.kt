package com.RiDsoft.kangwonhighschool.ui.main

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast

import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.ui.main.adapter.MainAdapter
import com.RiDsoft.kangwonhighschool.ui.preview.InitializeActivity
import com.RiDsoft.kangwonhighschool.ui.schedule.student.ScheduleActivity
import com.RiDsoft.kangwonhighschool.ui.announce.AnnounceActivity
import com.RiDsoft.kangwonhighschool.ui.main.adapter.MainActivityObject
import com.RiDsoft.kangwonhighschool.ui.main.features.MapActivity
import com.RiDsoft.kangwonhighschool.ui.meal.MealActivity
import com.RiDsoft.kangwonhighschool.ui.id.StudentIDActivity
import com.RiDsoft.kangwonhighschool.ui.schedule.teacher.TeacherScheduleActivity
import com.RiDsoft.kangwonhighschool.ui.secure.LogInActivity
import com.RiDsoft.kangwonhighschool.ui.user.settings.SettingActivity
import com.RiDsoft.kangwonhighschool.ui.web.WebActivity
import com.RiDsoft.kangwonhighschool.value.DataManager
import com.RiDsoft.kangwonhighschool.value.Key
import com.google.firebase.iid.FirebaseInstanceId
import java.util.*

/**
 * Created by RiD on 2017. 4. 9..
 */

class MainActivity : AppCompatActivity() {

    private lateinit var mDlDrawer: DrawerLayout
    private lateinit var mDtToggle: ActionBarDrawerToggle

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainAdapter

    private lateinit var userPref: SharedPreferences
    private lateinit var appPref: SharedPreferences
    private lateinit var defaultPref: SharedPreferences
    private var grade: String? = null
    private var classNumber: String? = null

    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var classArray: ArrayList<String>

    var isTeacher = false

    private val scheduleData: String
        get() {
            val setting = PreferenceManager.getDefaultSharedPreferences(this)
            var s = DataManager(this).scheduleOfDay
            for (i in 1..3) {
                val c = setting.getString("pref_pick$i", "선$i")!!.replace("0", "선$i")
                s = s.replace("선$i", c)
            }
            return s
        }

    private val mealData: String
        get() {
            val getter = DataManager(this)
            return getter.mealData
        }

    private fun getData(): Array<MainActivityObject> {
        return arrayOf(
                MainActivityObject(MainAdapter.ROW_DETAIL, getString(R.string.menu_schedule), scheduleData,
                        BitmapFactory.decodeResource(resources, R.drawable.ic_assignment_green_24dp)) {

                    val type = if (userPref.getInt(Key.KEY_USER_GRADE, 1) == 1) 0 else 1
                    val grade = userPref.getInt(Key.KEY_USER_GRADE, 1)
                    val classNum = userPref.getInt(Key.KEY_USER_CLASS, 1)

                    if (grade == 0 && classNum == 0) {
                        startActivity(Intent(this, TeacherScheduleActivity::class.java))
                    } else {
                        val intent = Intent(this, ScheduleActivity::class.java)
                        intent.putExtra("type", type)
                        intent.putExtra("grade", grade)
                        intent.putExtra("class", classNum)
                        startActivity(intent)
                    }

                },
                MainActivityObject(MainAdapter.ROW_DETAIL, getString(R.string.menu_meal), mealData,
                        BitmapFactory.decodeResource(resources, R.drawable.ic_local_dining_orange_24dp)) {
                    startActivity(Intent(this, MealActivity::class.java))
                },
                MainActivityObject(MainAdapter.ROW_MENU, getString(R.string.menu_announcement), null,
                        BitmapFactory.decodeResource(resources, R.drawable.ic_notifications_black_48dp)) {
                    startActivity(Intent(this, AnnounceActivity::class.java))
                },
                MainActivityObject(MainAdapter.ROW_MENU, getString(R.string.menu_map), null,
                        BitmapFactory.decodeResource(resources, R.drawable.ic_map_black_48dp)) {
                    startActivity(Intent(this, MapActivity::class.java))
                },
                MainActivityObject(MainAdapter.ROW_MENU, getString(R.string.menu_web), null,
                        BitmapFactory.decodeResource(resources, R.drawable.ic_language_black_48dp)) {
                    startActivity(Intent(this, WebActivity::class.java))
                }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val mToolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(mToolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
        getUserInfo()
        setToolbarTitle()

        appPref = getSharedPreferences(Key.KEY_APP, 0)
        defaultPref = PreferenceManager.getDefaultSharedPreferences(this)

        mDlDrawer = findViewById(R.id.drawer_layout)
        mDtToggle = object : ActionBarDrawerToggle(this, mDlDrawer, R.string.app_name, R.string.app_name) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
            }
        }
        mDlDrawer.setDrawerListener(mDtToggle)

        recyclerView = findViewById(R.id.recyclerview_main)

        initRecyclerView()

        setArrayAdapter()
        setFab()

        recommendSmallScreenMode()

        if (!checkInitialized()) {
            startActivity(Intent(this, InitializeActivity::class.java))
            this.finish()
        }

        if (defaultPref.getBoolean("pref_app_lock", false)) {
            startActivity(Intent(this, LogInActivity::class.java))
        }

    }

    override fun onResume() {
        super.onResume()
        adapter.setData(getData())
    }

    private fun checkInitialized(): Boolean {
        val initializePref = getSharedPreferences(InitializeActivity.KEY_INITIALIZE, 0)
        return initializePref.getBoolean(InitializeActivity.BOOL_NOTIFICATION_CHANNEL, false)
    }

    private fun getUserInfo() {
        userPref = getSharedPreferences(Key.KEY_USER, 0)
        if (userPref.getInt("grade", 1) == 0 &&
                userPref.getInt("class", 1) == 0) {
            isTeacher = true
        }
        grade = userPref.getInt("grade", 1).toString()
        classNumber = userPref.getInt("class", 1).toString()
    }

    private fun setToolbarTitle() {
        val name = userPref.getString("name", "")
        if (isTeacher) {
            supportActionBar?.title = "$name ${getString(R.string.teacher)}"
        } else {
            val n = userPref.getInt("number", 1)
            val number: String
            number = if (n < 10) {
                "0" + n.toString()
            } else {
                n.toString()
            }
            supportActionBar?.title = "$grade$classNumber$number $name"
        }
    }

    private fun initRecyclerView() {
        if (defaultPref.getBoolean("pref_low_dpi", false)) {
            recyclerView.layoutManager = LinearLayoutManager(this)
        } else {
            val layoutManager = GridLayoutManager(this, 2)
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (position) {
                        0 -> 1
                        1 -> 1
                        else -> 2
                    }
                }
            }
            recyclerView.layoutManager = layoutManager
        }
        recyclerView.setHasFixedSize(true)
        adapter = MainAdapter(this)
        adapter.setData(getData())
        recyclerView.adapter = adapter
    }

    private fun setFab() {
        val fab = findViewById<View>(R.id.fab_other_schedule) as FloatingActionButton
        fab.setOnClickListener {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setTitle("다른 시간표 보기")
                    .setAdapter(arrayAdapter) { dialogInterface, i ->
                        dialogInterface.dismiss()
                        val s = arrayAdapter.getItem(i)
                        val ss = s!!.split("학년 ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        val grade = Integer.parseInt(ss[0])
                        val classN = Integer.parseInt(ss[1].trim { it <= ' ' }.replace("반", ""))
                        val intent = Intent(this@MainActivity, ScheduleActivity::class.java)
                        intent.putExtra("grade", grade)
                        intent.putExtra("class", classN)
                        startActivity(intent)
                    }
                    .setNegativeButton("취소") { dialogInterface, i -> dialogInterface.dismiss() }
                    .show()
        }
    }

    private fun setArrayAdapter() {
        arrayAdapter = ArrayAdapter(
                this,
                android.R.layout.select_dialog_singlechoice)

        for (s in setArrayData()) {
            arrayAdapter.add(s)
        }
    }

    private fun setArrayData(): ArrayList<String> {
        classArray = ArrayList()
        val getter = DataManager(this)

        for (i in 0 until getter.classNumbers[0]) {
            classArray.add("1학년 " + (i + 1) + "반")
        }

        for (i in 0 until getter.classNumbers[1]) {
            classArray.add("2학년 " + (i + 1) + "반")
        }

        for (i in 0 until getter.classNumbers[2]) {
            classArray.add("3학년 " + (i + 1) + "반")
        }

        return classArray
    }

    override fun onPostResume() {
        super.onPostResume()
        if (defaultPref.getBoolean("pref_low_dpi", false)) {
            recyclerView.layoutManager = LinearLayoutManager(this)
        } else {
            val layoutManager = GridLayoutManager(this, 2)
            layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (position) {
                        0 -> 1
                        1 -> 1
                        else -> 2
                    }
                }
            }
            recyclerView.layoutManager = layoutManager
        }
        recyclerView.adapter = adapter
    }

    private fun recommendSmallScreenMode() {
        val settingPref = PreferenceManager.getDefaultSharedPreferences(this)
        val appPref = getSharedPreferences(Key.KEY_APP, 0)
        val settingPrefEditor = settingPref.edit()
        val appPrefEditor = appPref.edit()

        val dm = applicationContext.resources.displayMetrics
        val width = dm.widthPixels

        if (width < 960) {

            if (!settingPref.getBoolean(Key.PREF_SMALL_SCREEN_MODE, false) && !appPref.getBoolean(Key.PREF_SMALL_SCREEN_RECOMMEND, false)) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("작은 화면 모드")
                        .setMessage("현재 DPI가 작은 화면을 사용중입니다." +
                                "\n\n새로 추가된 \'작은 화면 모드\'를 사용하면 사용성을 개선할 수 있습니다." +
                                "\n\n작은 화면 모드로 설정할까요?" +
                                "\n\n(추후 설정에서 끌 수 있습니다)")
                        .setPositiveButton("사용") { _, _ ->
                            settingPrefEditor.putBoolean(Key.PREF_SMALL_SCREEN_MODE, true)
                            appPrefEditor.putBoolean(Key.PREF_SMALL_SCREEN_RECOMMEND, true)
                            settingPrefEditor.apply()
                            appPrefEditor.apply()
                            finish()
                            Toast.makeText(this@MainActivity, "재시작해주세요.", Toast.LENGTH_SHORT).show()
                        }
                        .setNegativeButton("사용 안함") { dialogInterface, i ->
                            settingPrefEditor.putBoolean(Key.PREF_SMALL_SCREEN_MODE, false)
                            appPrefEditor.putBoolean(Key.PREF_SMALL_SCREEN_RECOMMEND, true)
                            settingPrefEditor.apply()
                            appPrefEditor.apply()
                            dialogInterface.dismiss()
                        }
                        .setCancelable(false)
                        .show()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        if (isTeacher) {
            menu.removeItem(R.id.action_student_id)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (mDtToggle.onOptionsItemSelected(item)) {
            return true
        }

        when (item.itemId) {
            R.id.action_settings ->
                startActivity(Intent(this@MainActivity, SettingActivity::class.java))

            R.id.action_student_id ->
                startActivity(Intent(this@MainActivity, StudentIDActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mDtToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mDtToggle.onConfigurationChanged(newConfig)
    }

    override fun onBackPressed() {
        if (mDlDrawer.isDrawerOpen(GravityCompat.START)) {
            mDlDrawer.closeDrawer(GravityCompat.START, true)
        } else {
            super.onBackPressed()
        }
    }

}
