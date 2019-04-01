package com.RiDsoft.kangwonhighschool.ui.main.dashboard


import android.app.Activity
import android.content.*
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.ui.main.adapter.NavDrawerAdapter

import java.util.ArrayList

/**
 * Created by soc06_000 on 2015-03-08.
 */
class NavigationDrawerFragment : Fragment() {

    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<*>? = null
    private val mDatas = ArrayList<String>()
    private var mMemoPref: SharedPreferences? = null
    private var mAppPref: SharedPreferences? = null
    private var mAddLayout: FrameLayout? = null
    private var mEmptyView: LinearLayout? = null

    private val mTextView: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false)

        mAddLayout = view.findViewById<View>(R.id.layout_add_memo) as FrameLayout
        mEmptyView = view.findViewById<View>(R.id.layout_empty_view) as LinearLayout

        mMemoPref = context!!.getSharedPreferences("memo", 0)
        mAppPref = context!!.getSharedPreferences("app", 0)

        initData()
        setEmptyView()

        mRecyclerView = view.findViewById<View>(R.id.recycler_view_dashboard) as RecyclerView
        mRecyclerView!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        mRecyclerView!!.layoutManager = layoutManager

        mAdapter = NavDrawerAdapter(context!!, mDatas, object : NavDrawerAdapter.UICallBack {
            override fun onLongClick(position: Int, content: String) {
                val builder = AlertDialog.Builder(context!!)
                builder.setTitle("메모 삭제 혹은 복사")
                        .setMessage("메모를 삭제 또는 복사 하시겠습니까?")
                        .setCancelable(false)
                        .setNegativeButton("취소") { dialogInterface, i -> dialogInterface.dismiss() }
                        .setPositiveButton("삭제") { dialogInterface, i ->
                            mDatas.removeAt(position)
                            mAdapter!!.notifyDataSetChanged()
                            setEmptyView()
                            saveData()
                        }
                        .setNeutralButton("복사") { dialogInterface, i ->
                            copyContent(content)
                            dialogInterface.dismiss()
                        }
                        .show()
            }

            override fun onClick(position: Int) {
                val intent = Intent(context, MemoDetailActivity::class.java)
                intent.putExtra("position", position)
                startActivityForResult(intent, REQ_MEMO_DETAIL)
            }
        })

        mAddLayout!!.setOnClickListener {
            if (mAppPref!!.getInt("memo_index", 0) <= 15) {
                startActivityForResult(Intent(context, MemoWriteActivity::class.java), REQ_MEMO_EDIT)
            } else {
                Toast.makeText(context, "메모는 15개까지 추가할 수 있습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        mRecyclerView!!.adapter = mAdapter

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_MEMO_EDIT && resultCode == Activity.RESULT_OK) {
            mDatas.clear()
            initData()
            //mAdapter.notifyItemInserted(data.getIntExtra("memo_index", 0));
            mAdapter!!.notifyDataSetChanged()
            setEmptyView()
        } else if (requestCode == REQ_MEMO_DETAIL && resultCode == Activity.RESULT_OK) {
            mDatas.clear()
            initData()
            mAdapter!!.notifyDataSetChanged()
            setEmptyView()
        }
    }

    private fun copyContent(content: String) {
        val clipboardManager = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(content, content)
        clipboardManager.primaryClip = clipData
        Toast.makeText(context, "클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun initData() {
        for (i in 0 until mAppPref!!.getInt("memo_index", 0)) {
            mDatas.add(i, mMemoPref!!.getString(i.toString(), null))
        }
    }

    private fun setEmptyView() {
        if (mDatas.size == 0) {
            mEmptyView!!.visibility = View.VISIBLE
        } else {
            mEmptyView!!.visibility = View.GONE
        }
    }

    private fun saveData() {
        val editor = mMemoPref!!.edit()
        val editor1 = mAppPref!!.edit()
        editor.clear()
        editor1.putInt("memo_index", mDatas.size)
        for (i in mDatas.indices) {
            editor.putString(i.toString(), mDatas[i])
        }
        editor.apply()
        editor1.apply()
    }

    companion object {

        private val REQ_MEMO_EDIT = 100
        private val REQ_MEMO_DETAIL = 102
    }

}
