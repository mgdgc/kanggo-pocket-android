package com.RiDsoft.kangwonhighschool.ui.announce

import android.content.Intent
import java.util.ArrayList
import android.os.AsyncTask
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.ui.announce.view.ViewActivity
import com.RiDsoft.kangwonhighschool.worker.AnnounceParser
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_announce.*

class AnnounceActivity : AppCompatActivity() {

    private lateinit var adapter: AnnounceAdapter
    private val data: ArrayList<AnnounceObject> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announce)
        setSupportActionBar(toolbar_announce)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initTabLayout()
        initSwipeRefreshLayout()
        initRecyclerView()

        initData()
    }

    private fun initTabLayout() {
        tab_announce.addTab(tab_announce.newTab().setText("전체").setTag(0))
        tab_announce.addTab(tab_announce.newTab().setText("일반").setTag(1))
        tab_announce.addTab(tab_announce.newTab().setText("학적").setTag(2))
        tab_announce.addTab(tab_announce.newTab().setText("행사안내").setTag(3))
        tab_announce.addTab(tab_announce.newTab().setText("업데이트").setTag(4))

        tab_announce.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.tag is Int) {
                    filterData(tab.tag as Int)
                } else {
                    filterData(0)
                }
            }

        })
    }

    private fun initSwipeRefreshLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            srl_announce.setColorSchemeColors(
                    resources.getColor(R.color.refresh_orange, null),
                    resources.getColor(R.color.refresh_yellow, null),
                    resources.getColor(R.color.refresh_green, null),
                    resources.getColor(R.color.refresh_blue_grey, null)
            )
        }

        srl_announce.setOnRefreshListener {
            tab_announce.getTabAt(0)?.select()
            initData()
        }
    }

    private fun initRecyclerView() {
        rv_announce.layoutManager = LinearLayoutManager(this)
        rv_announce.setHasFixedSize(true)

        adapter = AnnounceAdapter()
        adapter.setOnItemClickListener { position, data ->
            val intent = Intent(this@AnnounceActivity, ViewActivity::class.java)
            intent.putExtra("object", Gson().toJson(data))
            startActivity(intent)
        }

        rv_announce.adapter = adapter
    }

    private fun initData() {
        val task = AnnounceTask()

        task.setUICallBack({
            srl_announce.isRefreshing = true
        }, { result ->
            srl_announce.isRefreshing = false

            if (result != null) {
                this.data.clear()
                this.data.addAll(result)
                adapter.setData(result)
            }
        })

        task.execute()
    }

    private fun filterData(category: Int) {
        val data: ArrayList<AnnounceObject> = ArrayList()

        for (a in this.data) {
            if (category == 0 || a.category == category) {
                data.add(a)
            }
        }

        adapter.setData(data)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return if (item?.itemId == android.R.id.home) {
            finish()
            true
        } else super.onOptionsItemSelected(item)

    }
}

class AnnounceTask : AsyncTask<Void, Int, ArrayList<AnnounceObject>>() {

    interface UICallBack {
        fun preExecute()
        fun postExecute(result: ArrayList<AnnounceObject>?)
    }

    fun setUICallBack(listener1: () -> Unit, listener2: (ArrayList<AnnounceObject>?) -> Unit) {
        this.callBack = object : UICallBack {
            override fun preExecute() {
                listener1()
            }

            override fun postExecute(result: ArrayList<AnnounceObject>?) {
                listener2(result)
            }

        }
    }

    private var callBack: UICallBack? = null

    override fun onPreExecute() {
        super.onPreExecute()
        callBack?.preExecute()
    }

    override fun doInBackground(vararg params: Void?): ArrayList<AnnounceObject>? {
        val parser = AnnounceParser()
        val doc = parser.getDocument(AnnounceParser.URL_ANNOUNCE_LIST) ?: return null
        return parser.getAnnouncementList(doc)
    }

    override fun onPostExecute(result: ArrayList<AnnounceObject>?) {
        super.onPostExecute(result)
        callBack?.postExecute(result)
    }
}