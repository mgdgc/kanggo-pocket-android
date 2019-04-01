package com.RiDsoft.kangwonhighschool.ui.developer

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.RiDsoft.kangwonhighschool.R

/**
 * Created by RiD on 2017. 10. 20..
 */

class DevModeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dev_mode)

        fragmentManager.beginTransaction().replace(R.id.layout_dev_mode, DevModeFragment()).commit()
    }
}
