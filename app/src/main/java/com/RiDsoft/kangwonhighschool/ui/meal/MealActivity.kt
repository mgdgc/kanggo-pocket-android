package com.RiDsoft.kangwonhighschool.ui.meal

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast

import com.RiDsoft.kangwonhighschool.R
import toast.library.meal.v7.MealLibrary

import java.util.Calendar

class MealActivity : AppCompatActivity() {

    private var mealPref: SharedPreferences? = null
    private var mEditor: SharedPreferences.Editor? = null

    private var connectivityManager: ConnectivityManager? = null

    private var mProgressDialog: ProgressDialog? = null

    private val listener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        if (isConnected) {
            if (isData) {
                val builder = AlertDialog.Builder(this@MealActivity)
                builder.setTitle("데이터 네트워크로 접속중")
                        .setMessage("데이터 네트워크에 연결되어 있습니다. " +
                                "다른 날짜의 급식을 불러올 때 데이터 통화료가 발생할 수 있습니다. " +
                                "계속하시겠습니까? ")
                        .setCancelable(false)
                        .setPositiveButton("확인") { dialogInterface, i ->
                            dialogInterface.dismiss()
                            onDatePickComplete(year, monthOfYear, dayOfMonth)
                        }
                        .setNegativeButton("취소") { dialogInterface, i -> dialogInterface.dismiss() }
                        .show()
            } else {
                onDatePickComplete(year, monthOfYear, dayOfMonth)
            }
        } else {
            val builder = AlertDialog.Builder(this@MealActivity)
            builder.setTitle("네트워크 연결 필요")
                    .setMessage("날짜를 선택하여 급식을 불러오는 데에는 네트워크 연결이 요구됩니다. " + "WiFi를 연결하거나 모바일 데이터 네트워크에 연결하시기 바랍니다.")
                    .setCancelable(false)
                    .setPositiveButton("확인") { dialogInterface, i -> dialogInterface.dismiss() }
                    .setNeutralButton("WiFi 설정") { dialogInterface, i ->
                        dialogInterface.dismiss()
                        val intent = Intent()
                        intent.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings")
                        startActivity(intent)
                        finish()
                    }
                    .show()
        }
    }

    private val week: Int
        get() {
            val calendar = Calendar.getInstance()
            return calendar.get(Calendar.DAY_OF_WEEK)
        }

    private val day: Int
        get() {
            val calendar = Calendar.getInstance()
            return calendar.get(Calendar.DAY_OF_MONTH)
        }

    private val year: Int
        get() {
            val calendar = Calendar.getInstance()
            return calendar.get(Calendar.YEAR)
        }

    private val month: Int
        get() {
            val calendar = Calendar.getInstance()
            return calendar.get(Calendar.MONTH) + 1
        }

    private val isConnected: Boolean
        get() {
            return try {
                val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val connInfo = connectivityManager.activeNetworkInfo
                connInfo != null
            } catch (e: Exception) {
                false
            }
        }

    private val isData: Boolean
        get() {
            val networkData = connectivityManager!!.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            return networkData != null && networkData.isConnected
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "급식"

        mealPref = getSharedPreferences("meal", 0)
        mEditor = mealPref!!.edit()

        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val fab = findViewById<View>(R.id.fab_meal) as FloatingActionButton
        fab.setOnClickListener {
            val dialog = DatePickerDialog(this@MealActivity, listener, year, month - 1, day)
            dialog.show()
        }

        val tabLayout = findViewById<View>(R.id.tablayout_meal) as TabLayout
        tabLayout.addTab(tabLayout.newTab().setText("월요일"))
        tabLayout.addTab(tabLayout.newTab().setText("화요일"))
        tabLayout.addTab(tabLayout.newTab().setText("수요일"))
        tabLayout.addTab(tabLayout.newTab().setText("목요일"))
        tabLayout.addTab(tabLayout.newTab().setText("금요일"))

        val viewPager = findViewById<View>(R.id.view_pager_meal) as ViewPager
        val pagerAdapter = MealTabAdapter(supportFragmentManager, tabLayout.tabCount)
        viewPager.adapter = pagerAdapter
        viewPager.setOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout.setOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        val ctl = findViewById<View>(R.id.toolbar_layout) as CollapsingToolbarLayout
        ctl.setExpandedTitleColor(Color.parseColor("#00000000"))
        ctl.setCollapsedTitleTextColor(Color.rgb(0, 0, 0))

        when (week) {
            2 -> viewPager.currentItem = 0
            3 -> viewPager.currentItem = 1
            4 -> viewPager.currentItem = 2
            5 -> viewPager.currentItem = 3
            6 -> viewPager.currentItem = 4
            1 -> viewPager.currentItem = 0
        }

        if (shouldUpdate()) {
            Log.d("should update", shouldUpdate().toString() + "")
            mEditor!!.putInt("update_week", week)
            mEditor!!.putInt("update_date", day)
            if (isConnected) {
                if (!isData) {
                    doParse()
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("데이터 네트워크로 접속중")
                            .setMessage("현재 데이터 네트워크로 접속중입니다. " +
                                    "데이터 통화료가 발생할 수 있습니다." +
                                    "급식 정보를 업데이트 하시겠습니까?")
                            .setCancelable(false)
                            .setPositiveButton("확인") { dialogInterface, i ->
                                dialogInterface.dismiss()
                                doParse()
                            }
                            .setNegativeButton("취소") { dialogInterface, i ->
                                dialogInterface.dismiss()
                                finish()
                            }
                            .setNeutralButton("WiFi 설정") { dialogInterface, i ->
                                dialogInterface.dismiss()
                                val intent = Intent()
                                intent.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings")
                                startActivity(intent)
                                finish()
                            }
                            .show()
                }
            } else {
                val builder = AlertDialog.Builder(this@MealActivity)
                builder.setTitle("네트워크 연결 필요")
                        .setMessage("현재 연결된 WiFi나 데이터 네트워크가 없습니다. " + "급식 정보를 최신 상태로 업데이트하려면 네트워크에 연결한 후 다시 시도해주세요.")
                        .setCancelable(false)
                        .setPositiveButton("확인") { dialogInterface, i -> dialogInterface.dismiss() }
                        .setNeutralButton("WiFi 설정") { dialogInterface, i ->
                            dialogInterface.dismiss()
                            val intent = Intent()
                            intent.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings")
                            startActivity(intent)
                            finish()
                        }
                        .show()
            }
        }

    }

    private fun onDatePickComplete(year: Int, month: Int, day: Int) {
        Log.d("datepicker", year.toString() + "/ " + month + "/ " + day)
        val intent = Intent(this@MealActivity, OtherDayMealActivity::class.java)
        intent.putExtra("year", year)
        intent.putExtra("month", month + 1)
        intent.putExtra("day", day)
        startActivity(intent)
    }

    private fun shouldUpdate(): Boolean {
        val updateWeek = mealPref!!.getInt("update_week", 0)
        val updateDate = mealPref!!.getInt("update_date", 0)
        val updateMondayDate = updateDate - updateWeek + 1
        val now = day - week + 1
        Log.d("now", (now - updateMondayDate).toString() + "")
        if (updateWeek == 0 && updateDate == 0) {
            Log.d("case", "1")
            return true
        } else if (now - updateMondayDate in 0..6) {
            Log.d("case", "4")
            return false
        } else {
            Log.d("case", "5")
            return true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_meal, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            android.R.id.home -> finish()

            R.id.action_allergy -> {
                val builder = AlertDialog.Builder(this@MealActivity)
                builder.setTitle("알러지 정보")
                        .setMessage("① 난류\n② 우유\n③ 메밀\n④ 땅콩\n⑤ 대두\n⑥ 밀\n⑦ 고등어\n⑧ 게\n⑨ 새우\n⑩ 돼지고기\n⑪ 복숭아\n⑫ 토마토\n⑬ 아황산염\n⑭ 호두\n⑮ 닭고기\n⑯ 소고기\n⑰ 오징어\n⑱ 조개류 (굴,전복,홍합포함)")
                        .setPositiveButton("닫기") { dialog, which -> dialog.dismiss() }
                        .show()
            }

            R.id.action_meal_update -> if (isConnected) {
                mEditor!!.putInt("update_week", week)
                mEditor!!.putInt("update_date", day)
                doParse()
            } else {
                Toast.makeText(this, "네트워크에 연결되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    private fun doParse() {
        val task = MealDownloadTask()
        task.setOnTaskStartedListener {
            mProgressDialog = ProgressDialog(this)
            mProgressDialog!!.setTitle("업데이트중")
            mProgressDialog!!.setMessage("급식 데이터를 다운로드 중입니다.")
            mProgressDialog!!.setCancelable(false)
            mProgressDialog!!.show()
        }
        task.setOnTaskFinishedListener {
            if (it == null) {
                return@setOnTaskFinishedListener
            }
            val calendar = it[0]
            val breakfast = it[1]
            val lunch = it[2]
            val dinner = it[3]
            for (i in 0..5) {
                mEditor!!.putString(i.toString() + "breakfast", breakfast[i])
                mEditor!!.putString(i.toString() + "lunch", lunch[i])
                mEditor!!.putString(i.toString() + "dinner", dinner[i])
                mEditor!!.putString(i.toString() + "date", calendar[i])
            }
            if (mProgressDialog != null && mProgressDialog!!.isShowing) {
                mProgressDialog!!.dismiss()
            }
            mEditor!!.apply()
            this@MealActivity.finish()
            startActivity(Intent(this@MealActivity, MealActivity::class.java))
        }
        task.execute(year, month, day, week)
    }
}

class MealDownloadTask : AsyncTask<Int, Int, Array<Array<String?>>?>() {

    private val countryCode = "kwe.go.kr" // 접속 할 교육청 도메인
    private val schulCode = "K100000364" // 학교 고유 코드
    private val schulCrseScCode = "4" // 학교 종류 코드 1
    private val schulKndScCode = "04" // 학교 종류 코드 2

    private var onTaskStartedListener: OnTaskStartedListener? = null
    private var onTaskFinishedListener: OnTaskFinishedListener? = null

    interface OnTaskStartedListener {
        fun onTaskStarted()
    }

    interface OnTaskFinishedListener {
        fun onFinished(result: Array<Array<String?>>?)
    }

    fun setOnTaskStartedListener(listener: () -> Unit) {
        this.onTaskStartedListener = object : OnTaskStartedListener {
            override fun onTaskStarted() {
                listener()
            }

        }
    }

    fun setOnTaskFinishedListener(listener: (Array<Array<String?>>?) -> Unit) {
        this.onTaskFinishedListener = object : OnTaskFinishedListener {
            override fun onFinished(result: Array<Array<String?>>?) {
                listener(result)
            }
        }
    }

    override fun onPreExecute() {
        super.onPreExecute()
        onTaskStartedListener?.onTaskStarted()
    }

    override fun doInBackground(vararg params: Int?): Array<Array<String?>>? {

        val year = Integer.toString(params[0]!!)
        var month = Integer.toString(params[1]!!)
        var day = Integer.toString(params[2]!!)

        if (month.length <= 1)
            month = "0$month"
        if (day.length <= 1)
            day = "0$day"

        lateinit var calender: Array<String?>
        lateinit var breakfast: Array<String?>
        lateinit var lunch: Array<String?>
        lateinit var dinner: Array<String?>

        try {

            calender = MealLibrary.getDateNew(countryCode, schulCode,
                    schulCrseScCode, schulKndScCode, "1", year, month, day)

            breakfast = MealLibrary.getMealNew(countryCode, schulCode,
                    schulCrseScCode, schulKndScCode, "1", year, month, day)

            lunch = MealLibrary.getMealNew(countryCode, schulCode,
                    schulCrseScCode, schulKndScCode, "2", year, month, day)

            dinner = MealLibrary.getMealNew(countryCode, schulCode,
                    schulCrseScCode, schulKndScCode, "3", year, month, day)

            return arrayOf(calender, breakfast, lunch, dinner)

        } catch (e: Exception) {
            Log.e("ProcessTask Error", "Message : " + e.message)
            Log.e("ProcessTask Error", "LocalizedMessage : " + e.localizedMessage)

            e.printStackTrace()
            return null
        }
    }

    override fun onPostExecute(result: Array<Array<String?>>?) {
        super.onPostExecute(result)
        onTaskFinishedListener?.onFinished(result)
    }

}