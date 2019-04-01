package com.RiDsoft.kangwonhighschool.ui.schedule.teacher

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import com.RiDsoft.kangwonhighschool.R


class TeacherScheduleEditDialog(context: Context, val defaultValue: TeacherScheduleObject?) : Dialog(context) {

    interface OnDismissListener {
        fun onDismiss(subject: String, classNum: String, memo: String)
    }

    fun setOnDismissListener(listener: (String, String, String) -> Unit) {
        this.onDismissListener = object : OnDismissListener {
            override fun onDismiss(subject: String, classNum: String, memo: String) {
                listener(subject, classNum, memo)
            }
        }
    }

    private var onDismissListener: OnDismissListener? = null

    private lateinit var editSubject: EditText
    private lateinit var editGrade: EditText
    private lateinit var editClass: EditText
    private lateinit var editMemo: EditText
    private lateinit var txtConfirm: TextView
    private lateinit var txtCancel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout_schedule_edit_dialog)

        editSubject = findViewById(R.id.et_edit_subject)
        editGrade = findViewById(R.id.et_edit_grade)
        editClass = findViewById(R.id.et_edit_class)
        editMemo = findViewById(R.id.et_edit_memo)
        txtConfirm = findViewById(R.id.txt_dialog_confirm)
        txtCancel = findViewById(R.id.txt_dialog_cancel)

        txtConfirm.setOnClickListener(onConfirmButtonClickListener)
        txtCancel.setOnClickListener(onCancelButtonClickListener)

        defaultValue?.let {
            editSubject.setText(it.subject)
            val classSplit = it.classNum.split("-")
            if (classSplit.size == 2) {
                editGrade.setText(classSplit[0])
                editClass.setText(classSplit[1])
            }
            editMemo.setText(it.memo)
        }

    }

    private val onConfirmButtonClickListener = View.OnClickListener {
        val subject = editSubject.text.toString().trim()
        val classNum = if (editGrade.text.toString().trim() == ""
                || editClass.text.toString().trim() == "") {
            ""
        } else {
            "${editGrade.text.toString().trim()} - ${editClass.text.toString().trim()}"
        }
        val memo = editMemo.text.toString().trim()
        onDismissListener?.onDismiss(subject, classNum, memo)
        dismiss()
    }

    private val onCancelButtonClickListener = View.OnClickListener {
        dismiss()
    }
}
