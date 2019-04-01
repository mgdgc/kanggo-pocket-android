package com.RiDsoft.kangwonhighschool.ui.main.dashboard

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.CheckBox

import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.ui.StartingActivity

import java.util.ArrayList

/**
 * Created by RiD on 2016. 12. 4..
 */

class MemoWriteActivity : AppCompatActivity() {

    private var mEditText: AppCompatEditText? = null
    private var mTextInputLayout: TextInputLayout? = null
    private var mMemoPref: SharedPreferences? = null
    private var mAppPref: SharedPreferences? = null
    private var mCheckBox: CheckBox? = null

    private val mText: String? = null
    private val mMemos = ArrayList<String>()
    private val mPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_edit)
        val toolbar = findViewById<Toolbar>(R.id.toolbar_memo_edit)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mMemoPref = getSharedPreferences("memo", 0)
        mAppPref = getSharedPreferences("app", 0)

        initData()

        mEditText = findViewById(R.id.edit_text_memo)
        mTextInputLayout = findViewById(R.id.text_input_memo)
        mCheckBox = findViewById(R.id.checkbox_memo_statusbar)

        mTextInputLayout!!.isCounterEnabled = true
        mTextInputLayout!!.counterMaxLength = 500

    }

    private fun initData() {
        for (i in 0 until mAppPref!!.getInt("memo_index", 0)) {
            mMemos.add(mMemoPref!!.getString(i.toString(), ""))
        }
    }

    fun intentSet(isSuccess: Boolean) {
        val intent = Intent()
        if (isSuccess) {
            intent.putExtra("memo_index", mMemos.size)
            setResult(RESULT_OK, intent)
        } else {
            setResult(RESULT_CANCELED)
        }
        finish()
    }

    private fun notification() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = PendingIntent.getActivities(this, 0, arrayOf(Intent(this, StartingActivity::class.java)), PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this)
        builder.setSmallIcon(R.drawable.notification_icon)
        builder.setTicker(mEditText!!.text.toString())
        builder.setWhen(System.currentTimeMillis())
        builder.setContentTitle(mEditText!!.text.toString())
        builder.setContentText("클릭하면 삭제됩니다.")
        builder.setAutoCancel(true)
        builder.setContentIntent(intent)
        builder.color = Color.parseColor("#f95631")

        manager.notify(103, builder.build())

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_edit_memo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when (item.itemId) {
            android.R.id.home -> intentSet(false)

            R.id.action_memo_cancel -> intentSet(false)

            R.id.action_memo_confirm -> {
                val editor = mMemoPref!!.edit()
                val editor1 = mAppPref!!.edit()

                mMemos.add(mEditText!!.text.toString())

                editor.clear()
                for (i in mMemos.indices) {
                    editor.putString(i.toString(), mMemos[i])
                }
                editor.apply()
                editor1.putInt("memo_index", mMemos.size)
                editor1.apply()

                if (mCheckBox!!.isChecked) {
                    notification()
                }

                intentSet(true)
            }
        }
        return true
    }
}
