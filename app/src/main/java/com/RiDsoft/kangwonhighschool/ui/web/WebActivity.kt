package com.RiDsoft.kangwonhighschool.ui.web

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.CookieSyncManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Spinner
import android.widget.Toast

import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.ui.web.adapter.WebAdapter
import com.RiDsoft.kangwonhighschool.ui.web.worker.WebTask
import com.RiDsoft.kangwonhighschool.ui.web.worker.WebValueObject
import com.RiDsoft.kangwonhighschool.value.Key
import com.RiDsoft.kangwonhighschool.value.WebKey

import java.util.ArrayList

/**
 * Created by RiD on 2016. 10. 24..
 */

class WebActivity : AppCompatActivity() {

    private var webPref: SharedPreferences? = null
    private var webTask: WebParseTask? = null

    private var srl: SwipeRefreshLayout? = null
    private var spinner: Spinner? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var webAdapter: WebAdapter? = null
    private var webLayout: FrameLayout? = null

    private var isLoggedIn = false
    private var boardID = WebKey.KEY_NOTIFICATION
    private var isProcessRefresh = true
    private var currentPage = 1
    private var currentUrl = fullUrl

    private var categoryIds: ArrayList<Int>? = null

    private val isWifi: Boolean
        get() {
            val isConnected: Boolean?
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            isConnected = networkWifi.isConnected
            return isConnected
        }

    private val isData: Boolean
        get() {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkData = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
            return networkData != null && networkData.isConnected
        }

    private val fullUrl: String
        get() {
            val baseUrl = "http://kanggo.net/boardCnts/list.do?"
            if (currentPage == 1) {
                isProcessRefresh = true
                currentUrl = baseUrl + "boardID=" + boardID + "&m=0201"
                return currentUrl
            } else {
                isProcessRefresh = false
                currentUrl = baseUrl + "type=default&page=" + currentPage + "&m=0201&s=ganggo&boardID=" + boardID
                return currentUrl
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        val toolbar = findViewById<View>(R.id.toolbar_web) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        webPref = getSharedPreferences(Key.KEY_WEB, 0)

        srl = findViewById<View>(R.id.srl_web) as SwipeRefreshLayout
        spinner = findViewById<View>(R.id.spinner_web) as Spinner
        webLayout = findViewById<View>(R.id.layout_web) as FrameLayout

        setRefreshLayout()
        setSpinner()

        recyclerView = findViewById<View>(R.id.recycler_view_web) as RecyclerView
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(this)

        webAdapter = WebAdapter(this, object : WebAdapter.OnClickListener {
            override fun onDocumentClick(id: Int) {
                val intent = Intent(this@WebActivity, WebDocActivity::class.java)
                intent.putExtra(WebDocActivity.KEY_URL, getDocumentUrl(id))
                startActivity(intent)
            }

            override fun onFooterClick() {
                currentPage++
                doTask()
            }
        })
        webAdapter!!.setScrollListener {
            currentPage++
            doTask()
        }

        adapter = webAdapter
        recyclerView!!.adapter = adapter

        CookieSyncManager.createInstance(this)

        doTask()

    }

    private fun setRefreshLayout() {
        srl!!.setColorSchemeResources(
                R.color.refresh_orange,
                R.color.refresh_yellow,
                R.color.refresh_green,
                R.color.refresh_blue_grey
        )

        srl!!.setOnRefreshListener {
            currentPage = 1
            doTask()
        }
    }

    private fun setSpinner() {
        val categoryAdapter = ArrayAdapter<Any>(
                this@WebActivity,
                android.R.layout.simple_spinner_dropdown_item
        )
        categoryIds = ArrayList()

        val webKey = WebKey()

        for (i in 0 until webKey.categoryArray.size) {
            categoryAdapter.add(webKey.categoryTitles[i])
            categoryIds!!.add(webKey.categoryArray[i])
        }

        spinner!!.adapter = categoryAdapter
        spinner!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                if (categoryIds!![position] != 0) {
                    boardID = categoryIds!![position]
                    currentPage = 1
                    doTask()
                } else {
                    Toast.makeText(this@WebActivity, "게시판을 선택해 주세요.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun getDocumentUrl(id: Int): String {
        return "http://kanggo.net/boardCnts/view.do?m=0301&boardID=" + boardID +
                "&boardSeq=" + id + "&lev=0&action=view"
    }

    private fun doTask() {
        if (isWifi || isData) {
            webTask = WebParseTask()
            webTask!!.execute(fullUrl)
        } else {
            Snackbar.make(webLayout!!, "네트워크에 연결되지 않았습니다.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("설정") {
                        try {
                            val intent = Intent()
                            intent.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings")
                            startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            e.printStackTrace()
                            val intent = Intent()
                            intent.setClassName("com.android.settings", "com.android.settings.Settings")
                            startActivity(intent)
                        }
                    }
                    .show()
            if (srl!!.isRefreshing) {
                srl!!.isRefreshing = false
            }
        }
    }

    private fun refreshPage() {
        currentPage = 1
        doTask()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_web, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            android.R.id.home -> finish()

            R.id.action_scroll_top -> recyclerView!!.scrollToPosition(0)

            R.id.action_refresh -> refreshPage()
        }
        return true
    }

    private fun checkLoginSuccess(msg: String): Boolean {
        return msg.contains("location.href=\"/main.do\";")
    }

    override fun onResume() {
        super.onResume()
        CookieSyncManager.getInstance().startSync()
    }

    override fun onPause() {
        super.onPause()
        CookieSyncManager.getInstance().stopSync()
    }

    private fun makeSnackBar(msg: String) {
        Snackbar.make(webLayout!!, msg, Snackbar.LENGTH_LONG)
                .setAction("다시 시도") { doTask() }
                .show()
    }

    private inner class WebParseTask : WebTask() {

        override fun onPreProceed() {
            if (!srl!!.isRefreshing) {
                srl!!.isRefreshing = true
            }
        }

        override fun onUpdate(progress: Int) {

        }

        override fun onFinish(vararg result: Int) {
            if (srl!!.isRefreshing) {
                srl!!.isRefreshing = false
            }

            if (result[0] == WebTask.RESULT_SUCCESS) {

                if (isProcessRefresh) {

                    if (webValueObjects != null) {

                        val wvoArr = ArrayList<WebValueObject>()
                        for (wvo in webValueObjects!!) {
                            wvoArr.add(wvo)
                        }

                        val wvos = wvoArr.toTypedArray<WebValueObject>()
                        if (adapter != null) {
                            webAdapter!!.updateData(*wvos)
                        }

                    } else {
                        Log.d("final result", "NO_CONTENT")
                        currentPage = 1
                    }

                } else {

                    if (webValueObjects != null) {

                        val wvoArr = ArrayList<WebValueObject>()
                        for (wvo in webValueObjects!!) {
                            wvoArr.add(wvo)
                        }

                        val wvos = wvoArr.toTypedArray<WebValueObject>()
                        if (adapter != null) {
                            webAdapter!!.addData(*wvos)
                        }

                    } else {
                        Log.d("final result", "NO_CONTENT")
                        currentPage = 1
                    }

                }

            } else if (result[0] == WebTask.RESULT_FAIL_404) {
                Log.d("final result", "RESULT_FAIL_404")
                currentPage = 1
                makeSnackBar("요청한 페이지를 찾을 수 없습니다.")

            } else if (result[0] == WebTask.RESULT_FAIL_IOEXCEPTION) {
                Log.d("final result", "RESULT_FAIL_IOEXCEPTION")
                currentPage = 1
                makeSnackBar("요청을 처리하는 도중에 오류가 발생했습니다.")

            } else if (result[0] == WebTask.RESULT_FAIL_URLEXCEPTION) {
                Log.d("final result", "RESULT_FAIL_URLEXCEPTION")
                currentPage = 1
                makeSnackBar("요청한 URL에 문제가 있습니다.")
            } else if (result[0] == WebTask.RESULT_LAST_CONTENT) {
                //더 이상 콘텐트가 없을 때
            }
        }
    }

}
