package com.RiDsoft.kangwonhighschool.ui.secure

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.RiDsoft.kangwonhighschool.R
import com.RiDsoft.kangwonhighschool.value.DataManager
import com.github.ajalt.reprint.core.AuthenticationFailureReason
import com.github.ajalt.reprint.core.AuthenticationListener
import com.github.ajalt.reprint.core.Reprint
import kotlinx.android.synthetic.main.activity_log_in.*


class LogInActivity : AppCompatActivity() {

    private var enteredPw = ""
    private var pwSettingMode: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        txt_pw_0.setOnClickListener(onClickListener)
        txt_pw_1.setOnClickListener(onClickListener)
        txt_pw_2.setOnClickListener(onClickListener)
        txt_pw_3.setOnClickListener(onClickListener)
        txt_pw_4.setOnClickListener(onClickListener)
        txt_pw_5.setOnClickListener(onClickListener)
        txt_pw_6.setOnClickListener(onClickListener)
        txt_pw_7.setOnClickListener(onClickListener)
        txt_pw_8.setOnClickListener(onClickListener)
        txt_pw_9.setOnClickListener(onClickListener)
        txt_pw_erase.setOnClickListener(onClickListener)
        txt_pw_confirm.setOnClickListener(onClickListener)

        getIntentData()

        if (pwSettingMode == 0
                && PreferenceManager.getDefaultSharedPreferences(this)
                        .getBoolean("pref_use_fingerprint", false)) {

            Reprint.initialize(this)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && Reprint.isHardwarePresent()
                    && Reprint.hasFingerprintRegistered()) {
                initReprint()
            } else {
                setPassCodeOnly()
            }
        } else {
            setPassCodeOnly()
        }

    }

    private fun getIntentData() {
        pwSettingMode = intent.getIntExtra("mode", 0)
        when (pwSettingMode) {
            0 -> {

            }
            1 -> {
                setPassCodeOnly()
                setMessage(getString(R.string.login_new_pw))
            }
            2 -> {
                setPassCodeOnly()
                setMessage(getString(R.string.login_current_pw))
            }
        }
    }

    private fun initReprint() {
        Reprint.authenticate(object : AuthenticationListener {
            override fun onFailure(failureReason: AuthenticationFailureReason?, fatal: Boolean, errorMessage: CharSequence?, moduleTag: Int, errorCode: Int) {
                when (failureReason) {
                    AuthenticationFailureReason.HARDWARE_UNAVAILABLE -> {
                        setMessage(getString(R.string.login_fingerprint_failed))
                    }
                    AuthenticationFailureReason.NO_HARDWARE -> {
                        setPassCodeOnly()
                    }
                    AuthenticationFailureReason.NO_FINGERPRINTS_REGISTERED -> {
                        setMessage(getString(R.string.login_wrong_fingerprint))
                    }
                    AuthenticationFailureReason.SENSOR_FAILED -> {
                        setMessage(getString(R.string.login_fingerprint_failed))
                    }
                    AuthenticationFailureReason.LOCKED_OUT -> {
                        setPassCodeOnly()
                    }
                    AuthenticationFailureReason.TIMEOUT -> {
                        img_fingerprint.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.baseline_lock_white_48))
                        txt_login_message.text = getString(R.string.login_default)
                    }
                    AuthenticationFailureReason.AUTHENTICATION_FAILED -> {
                        setMessage(getString(R.string.login_wrong_fingerprint))
                    }
                    AuthenticationFailureReason.UNKNOWN -> {
                        setMessage(getString(R.string.login_wrong_fingerprint))
                    }
                    null -> {
                        setMessage(getString(R.string.login_wrong_fingerprint))
                    }
                }
            }

            override fun onSuccess(moduleTag: Int) {
                this@LogInActivity.finish()
            }

        })
    }

    private val onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.txt_pw_0 -> {
                enteredPw += 0
            }
            R.id.txt_pw_1 -> {
                enteredPw += 1
            }
            R.id.txt_pw_2 -> {
                enteredPw += 2
            }
            R.id.txt_pw_3 -> {
                enteredPw += 3
            }
            R.id.txt_pw_4 -> {
                enteredPw += 4
            }
            R.id.txt_pw_5 -> {
                enteredPw += 5
            }
            R.id.txt_pw_6 -> {
                enteredPw += 6
            }
            R.id.txt_pw_7 -> {
                enteredPw += 7
            }
            R.id.txt_pw_8 -> {
                enteredPw += 8
            }
            R.id.txt_pw_9 -> {
                enteredPw += 9
            }
            R.id.txt_pw_erase -> {
                var prev = ""
                if (enteredPw.isEmpty()) {
                    return@OnClickListener
                }
                for (i in 0 until enteredPw.length - 1) {
                    prev += enteredPw[i]
                }
                enteredPw = prev
            }
            R.id.txt_pw_confirm -> {
                if (pwSettingMode == 0) checkPw()
                else if (pwSettingMode == 1 || pwSettingMode == 2) validatePw()
            }
        }
        if (enteredPw.length >= 4) {
            if (pwSettingMode == 0) checkPw()
            else if (pwSettingMode == 1 || pwSettingMode == 2) validatePw()
        }
        setPwView(enteredPw.length)
    }

    var settingLevel = 0
    var newPw = ""
    private fun validatePw() {
        val dataManager = DataManager(this)
        if (pwSettingMode == 1) {
            if (settingLevel == 0) {
                newPw = enteredPw
                setMessage(getString(R.string.login_new_pw_again))
            } else if (settingLevel == 1) {
                if (enteredPw == newPw) {
                    dataManager.setPassCode(newPw)
                    this.finish()
                } else {
                    settingLevel = 0
                    setMessage(getString(R.string.login_pw_wrong))
                    enteredPw = ""
                    return
                }
            }
        } else if (pwSettingMode == 2) {
            if (settingLevel == 0) {
                val current = dataManager.getPassCode()
                if (enteredPw == current) {
                    setMessage(getString(R.string.login_new_pw))
                } else {
                    settingLevel = 0
                    setMessage(getString(R.string.login_wrong_pw))
                    enteredPw = ""
                    return
                }
            } else if (settingLevel == 1) {
                newPw = enteredPw
                setMessage(getString(R.string.login_new_pw_again))
            } else if (settingLevel == 2) {
                if (enteredPw == newPw) {
                    dataManager.setPassCode(newPw)
                    this.finish()
                } else {
                    settingLevel = 0
                    setMessage(getString(R.string.login_pw_wrong))
                }
            }
        }
        settingLevel++
        enteredPw = ""
    }

    private fun checkPw() {
        val dataManager = DataManager(this)
        val pw = dataManager.getPassCode()
        if (enteredPw == pw) {
            this.finish()
        } else {
            setMessage(getString(R.string.login_wrong_pw))
        }
        enteredPw = ""
    }

    private fun setPwView(length: Int) {
        val txtInputs = arrayOf(txt_pw_input_1, txt_pw_input_2, txt_pw_input_3, txt_pw_input_4)
        for (t in txtInputs) {
            t.text = "_"
        }
        if (length > txtInputs.size) {
            enteredPw = ""
            return
        }
        for (i in 0 until length) {
            txtInputs[i].text = "*"
        }
    }

    private fun setPassCodeOnly() {
        img_fingerprint.setImageBitmap(BitmapFactory.decodeResource(resources, R.drawable.baseline_lock_white_48))
        txt_login_message.text = getString(R.string.login_default)
    }

    private fun setMessage(message: String) {
        txt_login_message.text = message
    }


    override fun onBackPressed() {

    }
}
