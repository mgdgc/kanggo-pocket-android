package com.RiDsoft.kangwonhighschool.ui.user

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*

import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.value.Key

/**
 * Created by RiD on 2016. 11. 20..
 */

class EditUserInfoActivity : AppCompatActivity() {

    private lateinit var policyBtn: TextView
    private lateinit var nameView: EditText
    private lateinit var gradeSpinner: Spinner
    private lateinit var classSpinner: Spinner
    private lateinit var numberView: EditText
    private lateinit var completeBtn: Button
    private lateinit var teacherSwitch: Switch

    private var grade: Int = 1
    private var classNum: Int = 1
    private var number: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_user_info)
        val toolbar = findViewById<View>(R.id.toolbar_edit_user) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        policyBtn = findViewById(R.id.txt_information_collect)
        nameView = findViewById(R.id.edit_user_name)
        gradeSpinner = findViewById(R.id.spinner_grade)
        classSpinner = findViewById(R.id.spinner_class)
        numberView = findViewById(R.id.edit_user_number)
        completeBtn = findViewById(R.id.btn_edit_user_complete)
        teacherSwitch = findViewById(R.id.switch_teacher)

        policyBtn.setOnClickListener {
            startActivity(Intent(this, StudentInformationActivity::class.java))
        }

        completeBtn.setOnClickListener {
            if (!checkValid()) {
                return@setOnClickListener
            }
            val builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.alert_check)
                    .setPositiveButton(R.string.confirm) { dialog, _ ->
                        saveUserInfo()
                        dialog.dismiss()
                    }
                    .setNegativeButton(R.string.cancel) { dialog, _ ->
                        dialog.dismiss()
                    }

            if (teacherSwitch.isChecked) {
                val msg = getString(R.string.alert_check_teacher_msg) +
                        "${nameView.text} ${getString(R.string.teacher)}"
                builder.setMessage(msg)
                        .show()
            } else {
                val msg = getString(R.string.alert_check_msg) +
                        "$grade${getString(R.string.grade)} $classNum${getString(R.string.classNum)} " +
                        "$number${getString(R.string.number)} ${nameView.text} ${getString(R.string.student)}"
                builder.setMessage(msg)
                        .show()
            }
        }

        classSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                classNum = position + 1
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        gradeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                grade = position + 1
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        initSpinner()
    }

    private fun initSpinner() {
        val gradeArray = ArrayList<String>()
        for (i in 0..2) {
            gradeArray.add(getString(R.string.grade_of).replace("(s)", (i + 1).toString()))
        }

        val classArray = ArrayList<String>()
        for (i in 0..10) {
            classArray.add(getString(R.string.class_of).replace("(s)", (i + 1).toString()))
        }

        gradeSpinner.adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, gradeArray)
        classSpinner.adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, classArray)
    }

    private fun checkValid(): Boolean {
        val name = nameView.text.toString()
        val number = numberView.text.toString()

        if (name.trim() == "") {
            makeSimpleAlert(getString(R.string.alert_check_info), getString(R.string.alert_enter_name))
        } else {
            if (teacherSwitch.isChecked) {
                return true
            }
            try {
                this.number = number.toInt()
                return true
            } catch (e: NumberFormatException) {
                makeSimpleAlert(getString(R.string.alert_check_info), getString(R.string.alert_enter_number))
            }
        }
        return false
    }

    private fun saveUserInfo() {
        val userPref = getSharedPreferences(Key.KEY_USER, 0)
        val userPrefEdit = userPref.edit()

        userPrefEdit.putString(Key.KEY_USER_NAME, nameView.text.toString())
        if (teacherSwitch.isChecked) {
            userPrefEdit.putInt(Key.KEY_USER_GRADE, 0)
            userPrefEdit.putInt(Key.KEY_USER_CLASS, 0)
            userPrefEdit.putInt(Key.KEY_USER_NUMBER, 0)
        } else {
            userPrefEdit.putInt(Key.KEY_USER_GRADE, grade)
            userPrefEdit.putInt(Key.KEY_USER_CLASS, classNum)
            userPrefEdit.putInt(Key.KEY_USER_NUMBER, number)
        }

        userPrefEdit.apply()

        this.finish()
        Toast.makeText(this, R.string.toast_save_complete, Toast.LENGTH_SHORT).show()
    }

    private fun makeSimpleAlert(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.confirm) { dialog, _ ->
                    dialog.dismiss()
                }
                .setCancelable(false)
                .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        when (item.itemId) {
            android.R.id.home -> this.finish()
        }
        return true
    }
}
