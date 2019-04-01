package com.RiDsoft.kangwonhighschool.ui.schedule.student

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView

import com.RiDsoft.kangwonhighschool.R

import java.util.ArrayList

/**
 * Created by RiD on 2017. 2. 28..
 */

class OtherScheduleChooseActivity : AppCompatActivity() {

    private var mListItem = ArrayList<String>()

    internal lateinit var appPref: SharedPreferences
    private var numFirst: Int = 0
    private var numSecond: Int = 0
    private var numThird: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_schedule)
        val toolbar = findViewById<View>(R.id.toolbar_other_schedule) as Toolbar
        setSupportActionBar(toolbar)

        appPref = getSharedPreferences("app", 0)

        initArray()

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mListItem)

        val listView = findViewById<View>(R.id.listview_class) as ListView
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this@OtherScheduleChooseActivity, ScheduleActivity::class.java)
            val position = i + 1
            if (position <= numFirst) {
                intent.putExtra("type", 0)
                intent.putExtra("grade", 1)
                intent.putExtra("class", position)
                Log.d("onClickScheduleClass", 1.toString() + "학년" + position + "반")
            } else if (position <= numSecond + numFirst && position > numFirst) {
                intent.putExtra("type", 1)
                intent.putExtra("grade", 2)
                intent.putExtra("class", position - numFirst)
                Log.d("onClickScheduleClass", 2.toString() + "학년" + (position - numFirst) + "반")
            } else {
                intent.putExtra("type", 1)
                intent.putExtra("grade", 3)
                intent.putExtra("class", position - numFirst - numSecond)
                Log.d("onClickScheduleClass", 3.toString() + "학년" + (position - numFirst - numSecond) + "반")
            }
            startActivity(intent)
        }
    }

    private fun initArray() {
        for (i in 1..numFirst) {
            mListItem.add("1학년 " + i + "반")
        }
        for (i in 1..numSecond) {
            mListItem.add("2학년 " + i + "반")
        }
        for (i in 1..numThird) {
            mListItem.add("3학년 " + i + "반")
        }
    }
}
