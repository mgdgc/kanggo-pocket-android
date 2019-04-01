package com.RiDsoft.kangwonhighschool.ui.user

import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient

import com.RiDsoft.kangwonhighschool.R

/**
 * Created by RiD on 2016. 12. 18..
 */

class StudentInformationActivity : AppCompatActivity() {
    private var webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_information)
        val toolbar = findViewById<Toolbar>(R.id.toolbar_student_information)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        webView = findViewById(R.id.webview_userinfo)

        setWebViewSetting()
    }

    private fun setWebViewSetting() {
        webView!!.settings.javaScriptEnabled = false
        webView!!.loadUrl(URL)
        webView!!.webViewClient = WebViewClient()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView!!.canGoBack()) {
            webView!!.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    companion object {

        private const val URL = "http://ridsoft.xyz/basic_policy.html"
    }
}
