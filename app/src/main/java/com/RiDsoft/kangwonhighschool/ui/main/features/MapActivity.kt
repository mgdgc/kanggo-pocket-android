package com.RiDsoft.kangwonhighschool.ui.main.features

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.RiDsoft.kangwonhighschool.R
import kotlinx.android.synthetic.main.activity_map.*
import uk.co.senab.photoview.PhotoViewAttacher

class MapActivity : AppCompatActivity() {

    private lateinit var map_01: Bitmap
    private lateinit var map_02: Bitmap
    private lateinit var map_03: Bitmap
    private lateinit var map_04: Bitmap
    private lateinit var map_00: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        setSupportActionBar(toolbar_map)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setMapBitmap()

        tabLayout.addTab(tabLayout.newTab().setText("1층").setTag(1), true)
        tabLayout.addTab(tabLayout.newTab().setText("2층").setTag(2))
        tabLayout.addTab(tabLayout.newTab().setText("3층").setTag(3))
        tabLayout.addTab(tabLayout.newTab().setText("4층").setTag(4))
        tabLayout.addTab(tabLayout.newTab().setText("지하").setTag(0))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.tag) {
                    1 -> img_map.setImageBitmap(map_01)
                    2 -> img_map.setImageBitmap(map_02)
                    3 -> img_map.setImageBitmap(map_03)
                    4 -> img_map.setImageBitmap(map_04)
                    0 -> img_map.setImageBitmap(map_00)
                }
            }

        })

        PhotoViewAttacher(img_map)

    }

    private fun setMapBitmap() {
        map_01 = BitmapFactory.decodeResource(resources, R.drawable.map_01)
        map_02 = BitmapFactory.decodeResource(resources, R.drawable.map_02)
        map_03 = BitmapFactory.decodeResource(resources, R.drawable.map_03)
        map_04 = BitmapFactory.decodeResource(resources, R.drawable.map_04)
        map_00 = BitmapFactory.decodeResource(resources, R.drawable.map_00)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            this.finish()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        map_01.recycle()
        map_02.recycle()
        map_03.recycle()
        map_04.recycle()
        map_00.recycle()
    }
}
