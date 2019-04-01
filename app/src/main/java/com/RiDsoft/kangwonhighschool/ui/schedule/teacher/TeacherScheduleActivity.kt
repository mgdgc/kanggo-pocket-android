package com.RiDsoft.kangwonhighschool.ui.schedule.teacher

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.value.DataManager
import java.util.ArrayList

class TeacherScheduleActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TeacherScheduleAdapter

    private var data: ArrayList<TeacherScheduleObject> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_schedule)
        val toolbar: Toolbar = findViewById(R.id.toolbar_teacher_schedule)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tabLayout = findViewById(R.id.tbl_teacher_schedule)
        recyclerView = findViewById(R.id.rv_teacher_schedule)

        initTabLayout()
        initRecyclerView()

        initData(0)
    }

    private fun initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText(R.string.monday))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tuesday))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.wednesday))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.thursday))
        tabLayout.addTab(tabLayout.newTab().setText(R.string.friday))

        tabLayout.tabGravity = TabLayout.GRAVITY_FILL
        tabLayout.setTabTextColors(resources.getColor(R.color.colorTextColorSecondary),
                resources.getColor(R.color.colorTextColor))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    initData(it.position)
                }
            }

        })

        tabLayout.getTabAt(0)!!.select()
    }

    private fun initRecyclerView() {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = TeacherScheduleAdapter(this)
        adapter.setOnItemClickListener { position ->
            val data = this.data[position]

            val builder = StringBuilder()
            builder.append("${getString(R.string.dialog_subject)}: ${data.subject}\n\n")
                    .append("${getString(R.string.dialog_class)}: ${data.classNum}\n\n")
                    .append("${getString(R.string.dialog_memo)}: \n${data.memo}")

            val dialog = AlertDialog.Builder(this)
            dialog.setTitle(R.string.dialog_detail)
                    .setMessage(builder.toString())
                    .setPositiveButton(R.string.confirm) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setNeutralButton(R.string.edit) { dialog, _ ->
                        dialog.dismiss()
                        buildEditDialog(position)
                    }
                    .show()
        }
        adapter.setOnItemLongClickListener {position ->
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle(R.string.dialog_edit)
                    .setMessage(R.string.dialog_edit_message)
                    .setPositiveButton(R.string.edit) { dialog, _ ->
                        buildEditDialog(position)
                        dialog.dismiss()
                    }
                    .setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setNeutralButton(R.string.remove) { dialog, _ ->
                        dialog.dismiss()
                        val manager = DataManager(this)
                        val data = data[position]
                        data.classNum = ""
                        data.subject = ""
                        data.memo = ""
                        manager.saveTeacherSchedule(data, position, tabLayout.selectedTabPosition)

                        this.data[position] = data
                        adapter.setData(this.data.toTypedArray())
                    }
                    .show()
        }

        recyclerView.adapter = adapter
    }

    private fun buildEditDialog(position: Int) {
        val editDialog = TeacherScheduleEditDialog(this, data[position])
        editDialog.setOnDismissListener { subject, classNum, memo ->
            val data = this.data[position]

            val dayOfWeek = tabLayout.selectedTabPosition

            data.period = position
            data.dayOfWeek = dayOfWeek
            data.subject = subject
            data.classNum = classNum
            data.memo = memo

            this.data[position] = data
            adapter.setData(this.data.toTypedArray())

            val manager = DataManager(this)
            manager.saveTeacherSchedule(data, position, dayOfWeek)
        }
        editDialog.setCancelable(false)
        editDialog.show()
    }

    private fun initData(position: Int) {
        val manager = DataManager(this)
        data.clear()
        data.addAll(manager.getTeacherSchedule(position))
        adapter.setData(data.toTypedArray())
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (it.itemId == android.R.id.home) {
                this.finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
