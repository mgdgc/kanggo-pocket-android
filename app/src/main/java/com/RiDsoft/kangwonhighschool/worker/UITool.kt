package com.RiDsoft.kangwonhighschool.worker

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.util.TypedValue
import android.view.View
import android.widget.TextView

import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.value.Key

/**
 * Created by RiD on 2017. 10. 11..
 */

class UITool(private val mContext: Context) {
    private val mSettingPref: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)

    private val textSizeValues: Array<String>
        get() = mContext.resources.getStringArray(R.array.array_text_size_value)

    private val textSizePref: Int
        get() {
            for (s in textSizeValues) {
                if (s == mSettingPref.getString(Key.PREF_TEXT_SIZE, "16")) {
                    return Integer.valueOf(s)!!
                }
            }
            return 16
        }

    fun setTextSize(view: TextView) {
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizePref.toFloat())
    }

    fun setTextSize(vararg views: TextView) {
        for (view in views) {
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSizePref.toFloat())
        }
    }
}
