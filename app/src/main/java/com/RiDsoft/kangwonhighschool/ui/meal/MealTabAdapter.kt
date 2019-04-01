package com.RiDsoft.kangwonhighschool.ui.meal

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

import com.RiDsoft.kangwonhighschool.ui.meal.fragment.FridayFragment
import com.RiDsoft.kangwonhighschool.ui.meal.fragment.MondayFragment
import com.RiDsoft.kangwonhighschool.ui.meal.fragment.ThursdayFragment
import com.RiDsoft.kangwonhighschool.ui.meal.fragment.TuesdayFragment
import com.RiDsoft.kangwonhighschool.ui.meal.fragment.WednesdayFragment

/**
 * Created by RiD on 2016. 12. 17..
 */

class MealTabAdapter(fm: FragmentManager, internal var mTabCount: Int) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> MondayFragment()
            1 -> TuesdayFragment()
            2 -> WednesdayFragment()
            3 -> ThursdayFragment()
            4 -> FridayFragment()
            else -> null
        }
    }

    override fun getCount(): Int {
        return mTabCount
    }
}
