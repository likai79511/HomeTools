package com.agera.hometools

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import com.agera.hometools.core.TaskDriver
import com.agera.hometools.login.LoginActivity
import com.agera.hometools.login.LoginImp
import com.agera.hometools.push.PushImp
import com.agera.hometools.utils.CommonUtils
import com.agera.hometools.utils.Constants

/**
 * Created by Agera on 2017/10/23.
 */
class LaunchActivity : BaseActivity() {
    var startTime: Long = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startTime = System.currentTimeMillis()
        checkAndAutoLogin()
    }

    private fun checkAndAutoLogin() {
        var tel: String = CommonUtils.instance().getData(Constants.USERNAME, "") as String
        var password: String = CommonUtils.instance().getData(Constants.PASSWORD, "") as String

        if (!TextUtils.isEmpty(tel) && !TextUtils.isEmpty(password)) {
            TaskDriver.instance().mCore.submit{
                LoginImp.instance().login(tel,password)
                        .ifFailedSendTo {
                            TaskDriver.instance().mainHandler.post{startActivity(Intent(this, LoginActivity::class.java))}
                        }
                        .ifSucceededSendTo {
                            //set push account
                            PushImp.instance().setPushAccount(this, tel)
                            MyApp.instance().userName = tel
                            TaskDriver.instance().mainHandler.post{startMainActivity(true)}
                        }
            }
        } else {
            startMainActivity(false)
        }
    }

    private fun startMainActivity(flag: Boolean) {

        var duration = System.currentTimeMillis() - startTime
        Handler().postDelayed({
            startActivity(Intent(this, if (flag) MainActivity::class.java else LoginActivity::class.java))
            finish()
        }, if (duration >= 2_000) 0 else 2_000 - duration)
    }
}