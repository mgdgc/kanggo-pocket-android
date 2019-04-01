package com.RiDsoft.kangwonhighschool.ui.preview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.WindowManager

import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.widget.IndicatorWidget

/**
 * Created by RiD on 2016. 10. 6..
 */

class PreviewActivity : FragmentActivity() {

    internal var MAX_PAGE = 2
    internal var fragment = Fragment()
    internal lateinit var viewPager: ViewPager
    internal lateinit var mIndicatorLayout: IndicatorWidget

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)

        mIndicatorLayout = findViewById(R.id.indicator_layout)

        viewPager = findViewById(R.id.view_pager_preview)

        mIndicatorLayout.setSelect(0)

        val adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getCount(): Int {
                return MAX_PAGE
            }

            override fun getItem(position: Int): Fragment? {
                if (position < 0 || MAX_PAGE <= position)
                    return null
                when (position) {
                    0 -> fragment = WelcomeFragment()
                    1 -> fragment = UserInfoSettingFragment()
                }
                return fragment
            }
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                mIndicatorLayout.setSelect(position)
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })

        viewPager.adapter = adapter
    }
}
