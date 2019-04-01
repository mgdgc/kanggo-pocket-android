package com.RiDsoft.kangwonhighschool.ui.id

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.value.Key
import com.google.zxing.integration.android.IntentIntegrator

class StudentIDCreatorActivity : AppCompatActivity() {

    private lateinit var txtWarning: TextView
    private lateinit var btnCreate: Button

    private lateinit var sIDPref: SharedPreferences
    private lateinit var sIDPrefEdit: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_id_creator)

        sIDPref = getSharedPreferences(Key.KEY_STUDENT_ID, 0)
        sIDPrefEdit = sIDPref.edit()

        txtWarning = findViewById(R.id.txt_warning)
        txtWarning.setOnClickListener(warningClickListener)

        btnCreate = findViewById(R.id.btn_create_id)
        btnCreate.setOnClickListener(onIDCreateClickListener)
    }

    private val warningClickListener = View.OnClickListener {
        txtWarning.text = getText(R.string.id_creator_warn)
    }

    private val onIDCreateClickListener = View.OnClickListener {
        IntentIntegrator(this).initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        val codeType = intentResult.formatName
        val codeData = intentResult.contents

        sIDPrefEdit.putString(Key.STRING_BARCODE_TYPE, codeType)
        sIDPrefEdit.putString(Key.STRING_BARCODE_DATA, codeData)
        sIDPrefEdit.apply()

        Toast.makeText(this, "전자 학생증을 생성했습니다.", Toast.LENGTH_SHORT).show()
        this.finish()

    }
}