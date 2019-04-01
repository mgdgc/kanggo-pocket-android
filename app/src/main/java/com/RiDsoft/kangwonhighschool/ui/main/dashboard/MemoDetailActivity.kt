package com.RiDsoft.kangwonhighschool.ui.main.dashboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.RiDsoft.kangwonhighschool.R

import java.util.ArrayList

/**
 * Created by RiD on 2016. 12. 4..
 */

class MemoDetailActivity : AppCompatActivity() {

    private val mMemos = ArrayList<String>()
    private var mMemoPref: SharedPreferences? = null
    private var mAppPref: SharedPreferences? = null
    private var mTextView: TextView? = null
    private var mText: String? = null
    private var mPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memo_detail)
        val toolbar = findViewById<View>(R.id.toolbar_memo) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mTextView = findViewById<View>(R.id.txt_memo) as TextView

        mMemoPref = getSharedPreferences("memo", 0)
        mAppPref = getSharedPreferences("app", 0)

        initData()
        intentData()

        mText = mMemos[mPosition]
        mTextView!!.text = mText
        mTextView!!.setOnLongClickListener {
            copyContent(mText)
            val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
            vibrator.vibrate(500)
            true
        }

    }

    private fun copyContent(content: String?) {
        val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(content, content)
        clipboardManager.primaryClip = clipData
        Toast.makeText(this, "클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun intentData() {
        mPosition = intent.getIntExtra("position", 0)
    }

    private fun initData() {
        for (i in 0 until mAppPref!!.getInt("memo_index", 0)) {
            mMemos.add(mMemoPref!!.getString(i.toString(), ""))
        }
    }

    private fun activityResult(isSuccess: Boolean) {
        if (isSuccess) {
            val intent = Intent()
            intent.putExtra("isSuccess", true)
            intent.putExtra("position", mPosition)
            setResult(RESULT_OK, intent)
            finish()
        } else {
            val intent = Intent()
            intent.putExtra("isSuccess", false)
            intent.putExtra("position", mPosition)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_memo, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            android.R.id.home -> activityResult(false)

            R.id.action_share_memo -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.addCategory(Intent.CATEGORY_DEFAULT)
                intent.putExtra(Intent.EXTRA_TITLE, "메모")
                intent.putExtra(Intent.EXTRA_TEXT, mText)
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "메모 공유"))
            }

            R.id.action_edit_memo -> {
                val intent1 = Intent(this@MemoDetailActivity, MemoEditActivity::class.java)
                intent1.putExtra("position", mPosition)
                startActivityForResult(intent1, REQ_EDIT)
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_EDIT) {
            if (resultCode == RESULT_OK) {
                activityResult(true)
            } else {
                activityResult(false)
            }
        }
    }

    companion object {
        private const val REQ_EDIT = 101
    }
}
