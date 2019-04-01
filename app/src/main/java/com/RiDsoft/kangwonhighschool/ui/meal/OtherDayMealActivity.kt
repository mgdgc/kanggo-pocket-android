package com.RiDsoft.kangwonhighschool.ui.meal

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView

import com.RiDsoft.kangwonhighschool.R

import java.util.Calendar

/**
 * Created by RiD on 2017. 1. 15..
 */

class OtherDayMealActivity : AppCompatActivity() {

    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private var mBreakfastTxt: TextView? = null
    private var mLunchTxt: TextView? = null
    private var mDinnerTxt: TextView? = null
    private var mDateTxt: TextView? = null

    private var mProgressDialog: ProgressDialog? = null

    private val week: Int
        get() {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month - 1)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            return calendar.get(Calendar.DAY_OF_WEEK) - 1
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meal_other_day)
        val toolbar = findViewById<View>(R.id.toolbar_meal_other) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        intentDataSet()

        mBreakfastTxt = findViewById<View>(R.id.txt_breakfast_content) as TextView
        mLunchTxt = findViewById<View>(R.id.txt_lunch_content) as TextView
        mDinnerTxt = findViewById<View>(R.id.txt_dinner_content) as TextView
        mDateTxt = findViewById<View>(R.id.txt_meal_date) as TextView

        val task = MealDownloadTask()
        task.setOnTaskStartedListener {
            mProgressDialog = ProgressDialog(this@OtherDayMealActivity)
            mProgressDialog!!.setTitle("업데이트중")
            mProgressDialog!!.setMessage("급식 데이터를 다운로드 중입니다.")
            mProgressDialog!!.setCancelable(false)
            mProgressDialog!!.show()
        }
        task.setOnTaskFinishedListener {
            mDateTxt!!.text = it!![0][week]
            mBreakfastTxt!!.text = it[1][week]
            mLunchTxt!!.text = it[2][week]
            mDinnerTxt!!.text = it[3][week]
            mProgressDialog!!.dismiss()
        }
        task.execute(year, month, day)
    }

    private fun intentDataSet() {
        val intent = intent
        year = intent.getIntExtra("year", getYear())
        month = intent.getIntExtra("month", getMonth())
        day = intent.getIntExtra("day", getDay())
    }

    private fun getDay(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    private fun getYear(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR)
    }

    private fun getMonth(): Int {
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.MONTH) + 1
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_meal, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()

            R.id.action_allergy -> {
                val builder = AlertDialog.Builder(this@OtherDayMealActivity)
                builder.setTitle("알러지 정보")
                        .setMessage("① 난류\n② 우유\n③ 메밀\n④ 땅콩\n⑤ 대두\n⑥ 밀\n⑦ 고등어\n⑧ 게\n⑨ 새우\n⑩ 돼지고기\n⑪ 복숭아\n⑫ 토마토\n⑬ 아황산염\n⑭ 호두\n⑮ 닭고기\n⑯ 소고기\n⑰ 오징어\n⑱ 조개류 (굴,전복,홍합포함)")
                        .setPositiveButton("닫기") { dialog, which -> dialog.dismiss() }
                        .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
