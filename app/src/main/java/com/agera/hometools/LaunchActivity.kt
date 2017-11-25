package com.agera.hometools

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import com.agera.hometools.login.LoginActivity
import com.agera.hometools.login.LoginImp
import com.agera.hometools.utils.CommonUtils
import com.agera.hometools.utils.Constants

/**
 * Created by mac on 2017/10/23.
 */
class LaunchActivity : Activity() {
    var startTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startTime = System.currentTimeMillis()
        checkAndAutoLogin()
    }

    fun checkAndAutoLogin() {
        var tel: String = CommonUtils.instance().getData(Constants.USERNAME, "") as String
        var password: String = CommonUtils.instance().getData(Constants.PASSWORD, "") as String

        if (!TextUtils.isEmpty(tel) && !TextUtils.isEmpty(password)) {
            LoginImp.instance().login(tel, password)
                    .ifSucceededSendTo {
                        startMainActivity(true)
                    }
        } else {
            //clear error account
            CommonUtils.instance().clearData(Constants.USERNAME)
            CommonUtils.instance().clearData(Constants.PASSWORD)
            startMainActivity(false)
        }
    }

    private fun startMainActivity(flag: Boolean) {
        var duration = System.currentTimeMillis() - startTime
        Log.e("---", "--duration:" + duration)
        Handler().postDelayed({
            startActivity(Intent(this, if (flag) MainActivity::class.java else LoginActivity::class.java))
            finish()
        }, if (duration >= 2_000) 0 else 2_000-duration)
    }
}