package com.RiDsoft.kangwonhighschool.ui.user

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity

import com.RiDsoft.kangwonhighschool.R
import kotlinx.android.synthetic.main.activity_thanks_to.*

/**
 * Created by soc06_000 on 2015-03-09.
 */
class ThanksToActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thanks_to)

        setThanksToButton()
        setLicenseButton()

    }

    private fun setThanksToButton() {
        btnThanksTo.setOnClickListener {
            val builder = AlertDialog.Builder(this@ThanksToActivity)
            builder.setTitle("제작 도움")
                    .setMessage("개발자: 최명근 (가람방송국)" +
                            "\n아이디어 제안: 최홍신, 박정찬 (외부학생), 신성일  " +
                            "\n도움: 윤재환")
                    .setCancelable(false)
                    .setPositiveButton("닫기") { dialog, which -> dialog.dismiss() }
                    .show()
        }
    }

    private fun setLicenseButton() {
        btnLicence.setOnClickListener {
            val builder1 = AlertDialog.Builder(this@ThanksToActivity)
            builder1.setTitle("라이센스")
                    .setMessage("라이브러리 및 참조 출처:" +
                            "\nicons: https://material.io/icons/" +
                            "\ncolors: https://material.io/tools/color/" +
                            "\n급식 파싱 라이브러리 (Mir): https://bitbucket.org/whdghks913/wondanghighschool" +
                            "\nJericho: http://jericho.htmlparser.net/docs/index.html" +
                            "\nPhotoView: https://github.com/chrisbanes/PhotoView" +
                            "\nreprint (fingerprint scanner): https://github.com/ajalt/reprint" +
                            "\nicons (Google material icons): https://material.io/icons/" +
                            "\nZXing: https://zxing.github.io/zxing/license.html" +
                            "\nGson (Google): https://github.com/google/gson" +
                            "\nReprint: https://github.com/ajalt/reprint")
                    .setCancelable(false)
                    .setPositiveButton("닫기") { dialog, which -> dialog.dismiss() }
                    .show()
        }

    }
}
