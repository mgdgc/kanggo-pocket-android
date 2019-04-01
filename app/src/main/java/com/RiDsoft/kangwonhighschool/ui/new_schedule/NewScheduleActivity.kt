package com.RiDsoft.kangwonhighschool.ui.new_schedule

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.RiDsoft.kangwonhighschool.R


class NewScheduleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_schedule)
        val toolbar = findViewById<Toolbar>(R.id.toolbar_schedule_new)
        setSupportActionBar(toolbar)

    }
}