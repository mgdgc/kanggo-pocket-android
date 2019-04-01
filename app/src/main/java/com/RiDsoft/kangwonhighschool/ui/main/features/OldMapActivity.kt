package com.RiDsoft.kangwonhighschool.ui.main.features

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView

import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.value.DataManager

import net.htmlparser.jericho.HTMLElementName
import net.htmlparser.jericho.Source

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

import uk.co.senab.photoview.PhotoViewAttacher

/**
 * Created by soc06_000 on 2015-03-06.
 */
class OldMapActivity : AppCompatActivity() {

    private val filepath = Environment.getExternalStorageDirectory().absolutePath + "/KP/.map/"

    private var imgSchoolMapView: ImageView? = null
    private var srl: SwipeRefreshLayout? = null

    private val savedMapData: Bitmap?
        get() {
            try {
                val file = File(filepath)
                return if (!file.exists()) {
                    null
                } else BitmapFactory.decodeFile(file.absolutePath + "map.png")
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }

        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_map)
        val toolbar = findViewById<Toolbar>(R.id.toolbar_old_map)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        srl = findViewById(R.id.srl_map)
        imgSchoolMapView = findViewById(R.id.img_school_map)

        setSRL()

        val attacher = PhotoViewAttacher(imgSchoolMapView!!)

        if (checkPermission()) {
            if (savedMapData != null) {
                imgSchoolMapView!!.setImageBitmap(savedMapData)
            } else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("교실배치도 없음")
                        .setMessage("저장된 교실배치도가 없습니다.\n\n서버에서 다운로드 하시겠습니까?\n(데이터 통화료가 발생할 수 있습니다.)")
                        .setPositiveButton("다운로드") { dialog, which ->
                            if (checkPermission()) {
                                startParse()
                            }
                        }
                        .setNegativeButton("취소") { dialog, which -> dialog.dismiss() }
                        .show()
            }
        }
    }

    private fun setSRL() {
        srl!!.isEnabled = false
        srl!!.isRefreshing = false
        srl!!.setOnRefreshListener { srl!!.isRefreshing = false }
    }

    private fun startParse() {
        if (!DataManager(this).isConnectedToNetwork) {
            Snackbar.make(findViewById(R.id.layout_map), R.string.network_not_available, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.network_retry) {
                        startParse()
                    }
                    .show()
            return
        }
        val task = SchoolMapDownloadTask()
        task.setOnPreExecuteListener(object : SchoolMapDownloadTask.OnPreExecuteListener {
            override fun onPreExecute() {
                srl!!.isRefreshing = true
            }
        })
        task.setOnFinishExecuteListener(object : SchoolMapDownloadTask.OnFinishExecuteListener {
            override fun onFinishExecute(bitmap: Bitmap) {
                srl!!.isRefreshing = false
                imgSchoolMapView!!.setImageBitmap(bitmap)

                try {
                    val path = File(filepath)
                    if (!path.exists()) {
                        if (!path.mkdirs()) {
                            Log.e("mkdirs", "failed")
                        }
                    }
                    val file = File(path.absolutePath + "map.png")
                    val os = FileOutputStream(file)
                    Log.d("d", os.toString())
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
                    os.flush()
                    os.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })
        task.execute()
    }

    private fun checkPermission(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true
            } else {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 101)
                return false
            }
        } else {
            return true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (permissions[0] == Manifest.permission.WRITE_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startParse()
            } else {
                val builder = AlertDialog.Builder(this@OldMapActivity)
                builder.setTitle("권한 거부됨")
                        .setMessage("교실배치도를 저장하고 불러오는데 해당 권한이 필요합니다.\n\n권한을 부여하시겠습니까?")
                        .setPositiveButton("확인") { dialog, which ->
                            checkPermission()
                            dialog.dismiss()
                        }
                        .setNegativeButton("취소") { dialog, which -> dialog.dismiss() }
                        .setCancelable(false)
                        .show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_map, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_update_map) {
            if (checkPermission()) {
                startParse()
            }
        } else if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    public override fun onDestroy() {
        super.onDestroy()
    }

}

internal class SchoolMapDownloadTask : AsyncTask<String, Int, Bitmap>() {
    private var onPreExecuteListener: OnPreExecuteListener? = null
    private var onFinishExecuteListener: OnFinishExecuteListener? = null

    interface OnPreExecuteListener {
        fun onPreExecute()
    }

    interface OnFinishExecuteListener {
        fun onFinishExecute(bitmap: Bitmap)
    }

    fun setOnPreExecuteListener(onPreExecuteListener: OnPreExecuteListener) {
        this.onPreExecuteListener = onPreExecuteListener
    }

    fun setOnFinishExecuteListener(onFinishExecuteListener: OnFinishExecuteListener) {
        this.onFinishExecuteListener = onFinishExecuteListener
    }

    override fun onPreExecute() {
        super.onPreExecute()
        if (onPreExecuteListener != null) {
            onPreExecuteListener!!.onPreExecute()
        }
    }

    override fun doInBackground(vararg strings: String): Bitmap? {
        val parser = SchoolMapParser()
        return parser.schoolMapBitmap
    }

    override fun onPostExecute(bitmap: Bitmap) {
        super.onPostExecute(bitmap)
        if (onFinishExecuteListener != null) {
            onFinishExecuteListener!!.onFinishExecute(bitmap)
        }
    }
}

internal class SchoolMapParser {

    private val mapUrl = "http://kanggo.net/sub/info.do?m=0110&s=ganggo"

    //            connection.setConnectTimeout(5000);
    val schoolMapBitmap: Bitmap?
        get() {

            try {
                val connection = URL(schoolMapURL).openConnection() as HttpURLConnection ?: return null

                connection.doInput = true
                connection.connect()

                val `is` = connection.inputStream

                return BitmapFactory.decodeStream(`is`)

            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }

        }

    private val schoolMapURL: String?
        get() {

            val source: Source

            try {
                source = Source(URL(mapUrl))
            } catch (e: MalformedURLException) {
                e.printStackTrace()
                return null
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }

            source.fullSequentialParse()

            val div = source.getAllElements(HTMLElementName.DIV)
            var index = 0
            for (e in div) {
                try {
                    val attValue = e.getAttributeValue("class")
                    if (attValue != null && attValue == "subContent_body") {
                        break
                    }
                } catch (err: Exception) {
                    err.printStackTrace()
                }

                index++
            }
            try {
                val img = div[index].getAllElements(HTMLElementName.IMG)

                return "http://kanggo.net" + img[0].getAttributeValue("src")
            } catch (e: Exception) {
                e.printStackTrace()
                return "http://kanggo.net/board/x_free/upload/20180309/IMG_173318.jpg"
            }

        }

}
