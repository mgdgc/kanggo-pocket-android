package com.RiDsoft.kangwonhighschool.ui.id

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.value.Key
import com.google.zxing.BarcodeFormat
import com.google.zxing.Writer
import com.google.zxing.WriterException
import com.google.zxing.oned.Code128Writer
import com.google.zxing.oned.Code39Writer
import com.google.zxing.oned.Code93Reader

class StudentIDActivity : AppCompatActivity() {

    private lateinit var userPref: SharedPreferences
    private lateinit var sIDPref: SharedPreferences

    private lateinit var nameView: TextView
    private lateinit var infoView: TextView
    private lateinit var barcodeView: ImageView
    private lateinit var closeView: ImageView
    private lateinit var editBtn: ViewGroup
    private lateinit var deleteBtn: ViewGroup

    private lateinit var windowParams: WindowManager.LayoutParams
    private var brightness: Float = 0.5f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_id)

        windowParams = window.attributes

        sIDPref = getSharedPreferences(Key.KEY_STUDENT_ID, 0)
        userPref = getSharedPreferences(Key.KEY_USER, 0)
        val name = userPref.getString(Key.KEY_USER_NAME, "")
        val grade = userPref.getInt("grade", 1).toString()
        val classNumber = userPref.getInt("class", 1).toString()
        val n = userPref.getInt("number", 1)
        val number: String
        number = if (n < 10) {
            "0" + n.toString()
        } else {
            n.toString()
        }

        nameView = findViewById(R.id.txt_name)
        infoView = findViewById(R.id.txt_user_info)
        barcodeView = findViewById(R.id.img_s_id)
        closeView = findViewById(R.id.img_btn_close)
        closeView.setOnClickListener { this.finish() }
        editBtn = findViewById(R.id.btn_edit)
        editBtn.setOnClickListener { startActivity(Intent(this, StudentIDCreatorActivity::class.java)) }
        deleteBtn = findViewById(R.id.btn_delete)
        deleteBtn.setOnClickListener {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("학생증 삭제")
                    .setMessage("전자 학생증 데이터를 삭제하시겠습니까?")
                    .setPositiveButton("삭제") { dialog, which ->
                        dialog.dismiss()
                        deleteID()
                    }
                    .setNegativeButton("취소") { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()
        }

        nameView.text = name
        infoView.text = "${grade}학년 ${classNumber}반 ${number}번"

    }

    override fun onPostResume() {
        super.onPostResume()
        brightness = windowParams.screenBrightness
        windowParams.screenBrightness = 1f
        window.attributes = windowParams

        barcodeView.setImageBitmap(makeBarCode())
    }

    override fun onPause() {
        super.onPause()
        windowParams.screenBrightness = brightness
        window.attributes = windowParams
    }

    private fun deleteID() {
        val editor = sIDPref.edit()
        editor.remove(Key.STRING_BARCODE_DATA)
        editor.remove(Key.STRING_BARCODE_TYPE)
        editor.apply()
        Toast.makeText(this, "학생증이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
        this.finish()
    }

    private fun makeBarCode(): Bitmap? {
        val width = 480
        val height = 200

        var bitmap: Bitmap? = null

        val codeData = sIDPref.getString(Key.STRING_BARCODE_DATA, "")
        val codeType = sIDPref.getString(Key.STRING_BARCODE_TYPE, "")
        if (codeType == "" || codeData == "") {
            startActivity(Intent(this, StudentIDCreatorActivity::class.java))
            this.finish()
            return null
        }

        val writer = getCodeWriter(codeType)

        try {
            val bm = writer.encode(codeData, getCodeFormat(codeType), width, height)
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

            for (i in 0 until width) {
                for (j in 0 until height) {
                    if (bm.get(i, j)) {
                        bitmap.setPixel(i, j, Color.BLACK)
                    } else {
                        bitmap.setPixel(i, j, Color.WHITE)
                    }
                }
            }
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return bitmap
    }

    private fun getCodeWriter(codeType: String): Writer {
        return when (codeType) {
            "CODE_39" -> Code39Writer()
            "CODE_128" -> Code128Writer()
            else -> Code39Writer()
        }
    }

    private fun getCodeFormat(codeType: String): BarcodeFormat {
        return when (codeType) {
            "CODE_39" -> BarcodeFormat.CODE_39
            "CODE_93" -> BarcodeFormat.CODE_93
            "CODE_128" -> BarcodeFormat.CODE_128
            else -> BarcodeFormat.CODE_39
        }
    }
}