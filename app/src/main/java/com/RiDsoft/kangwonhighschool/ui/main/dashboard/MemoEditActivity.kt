package com.RiDsoft.kangwonhighschool.ui.main.dashboard

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView

import com.RiDsoft.kangwonhighschool.R

import java.util.ArrayList

import android.view.View.GONE

/**
 * Created by RiD on 2016. 12. 12..
 */

class MemoEditActivity : AppCompatActivity() {

    private var mPosition: Int = 0

    private var mMemos: ArrayList<String>? = null
    private var mMemoPref: SharedPreferences? = null
    private var mAppPref: SharedPreferences? = null
    private var mTextLayout: TextInputLayout? = null
    private var mEditText: EditText? = null
    private var mText: String? = null
    private var mCheckBox: CheckBox? = null
    private var mTextView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_edit)
        val toolbar = findViewById<Toolbar>(R.id.toolbar_memo_edit)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mMemoPref = getSharedPreferences("memo", 0)
        mAppPref = getSharedPreferences("app", 0)

        intentDataSet()
        initData()

        mTextLayout = findViewById(R.id.text_input_memo)
        mEditText = findViewById(R.id.edit_text_memo)
        mCheckBox = findViewById(R.id.checkbox_memo_statusbar)
        mTextView = findViewById(R.id.txt_memo_notification)

        mCheckBox!!.visibility = GONE
        mTextView!!.visibility = GONE
        mTextLayout!!.isCounterEnabled = true
        mTextLayout!!.counterMaxLength = 500

        mText = mMemos!![mPosition]
        mEditText!!.setText(mText)

    }

    private fun intentDataSet() {
        mPosition = intent.getIntExtra("position", 0)
    }

    private fun initData() {
        mMemos = ArrayList()
        for (i in 0 until mAppPref!!.getInt("memo_index", 0)) {
            mMemos!!.add(mMemoPref!!.getString(i.toString(), "nullData"))
        }
    }

    private fun saveData() {
        mMemos!!.removeAt(mPosition)
        mMemos!!.add(mPosition, mEditText!!.text.toString())

        val editor = mMemoPref!!.edit()
        editor.clear()

        for (i in 0 until mAppPref!!.getInt("memo_index", 0)) {
            editor.putString(i.toString(), mMemos!![i])
        }

        editor.apply()
    }

    private fun activityResult(isSuccess: Boolean) {
        if (isSuccess) {
            setResult(RESULT_OK)
        } else {
            setResult(RESULT_CANCELED)
        }
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_edit_memo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            android.R.id.home -> activityResult(false)

            R.id.action_memo_cancel -> activityResult(false)

            R.id.action_memo_confirm -> {
                saveData()
                activityResult(true)
            }
        }
        return true
    }
}
