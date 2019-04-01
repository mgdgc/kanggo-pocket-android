package com.RiDsoft.kangwonhighschool.ui.meal.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.worker.UITool


/**
 * A simple [Fragment] subclass.
 */
class MondayFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_meal, container, false)

        val mealPref = activity!!.getSharedPreferences("meal", 0)

        val breakfastTxt: TextView
        val lunchTxt: TextView
        val dinnerTxt: TextView
        val dateTxt: TextView

        breakfastTxt = view.findViewById<View>(R.id.txt_breakfast_content) as TextView
        lunchTxt = view.findViewById<View>(R.id.txt_lunch_content) as TextView
        dinnerTxt = view.findViewById<View>(R.id.txt_dinner_content) as TextView
        dateTxt = view.findViewById<View>(R.id.txt_meal_date) as TextView

        breakfastTxt.text = mealPref.getString("1breakfast", "")
        lunchTxt.text = mealPref.getString("1lunch", "")
        dinnerTxt.text = mealPref.getString("1dinner", "")
        dateTxt.text = mealPref.getString("1date", "")

        val txtViewArr = arrayOf(
                dateTxt, breakfastTxt, lunchTxt, dinnerTxt
        )

        for (t in txtViewArr) {
            if (t.text.isEmpty()) {
                t.text = "정보 없음"
            }
        }

        val tool = UITool(context!!)
        tool.setTextSize(breakfastTxt, lunchTxt, dinnerTxt)

        return view
    }

}// Required empty public constructor
