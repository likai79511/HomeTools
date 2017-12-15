package com.agera.hometools.login

import android.util.Log
import android.widget.EditText
import com.agera.hometools.bean.LoginResponse
import com.agera.hometools.core.TaskDriver
import com.agera.hometools.network.Restful
import com.agera.hometools.utils.CommonUtils
import com.agera.hometools.utils.Constants
import com.google.android.agera.Result

/**
 * Created by 43992639 on 2017/11/24.
 */
class LoginImp private constructor() : LoginInter {
    companion object {
        var loginImp = LoginImp()
        fun instance(): LoginImp = loginImp
    }

    override fun checkTel(view: EditText?): Result<String> {
        if (view == null) return Result.failure()
        var tel = view.text.toString()
        return if (CommonUtils.instance().checkTel(tel)) {
            Result.success(tel)
        } else {
            CommonUtils.instance().showShortMessage(view, "telephone lenth is wrong")
            Result.failure()
        }
    }

    override fun checkPassword(view: EditText?): Result<String> {
        if (view == null) return Result.failure()
        var password = view.text.toString()
        return if (CommonUtils.instance().checkPassword(password)) {
            Result.success(password)
        } else {
            CommonUtils.instance().showShortMessage(view, "password length must is 6~11")
            Result.failure()
        }
    }

    override fun checkConfirmPassword(view: EditText?, password: String): Result<String> {
        if (view == null) return Result.failure()
        var password2 = view.text.toString()
        return if (CommonUtils.instance().checkConfirmPassword(password, password2)) {
            Result.success(password2)
        } else {
            CommonUtils.instance().showShortMessage(view, "两次密码必须一致")
            Result.failure()
        }
    }

    override fun login(tel: String, password: String): Result<String> {
        var task = Restful.instance().login(tel, password)
        TaskDriver.instance().execute(task)
        var result: Result<String>? = Result.failure()
        task.get()
                .ifFailedSendTo { Result.failure<String>(it) }
                .ifSucceededSendTo {
                    Log.e("----","----it-01:${it.bodyString.get()}")
                    try {

                        Log.e("----", "----it-02:${CommonUtils.instance().gson.fromJson(it.bodyString.get(), LoginResponse::class.java)}")
                    }catch (e:Exception){
                        Log.e("---","--error:${e.message}")
                    }

                    if (it.responseCode > 400 || CommonUtils.instance().gson.fromJson(it.bodyString.get(), LoginResponse::class.java).results.size < 1) {
                        CommonUtils.instance().clearData(Constants.USERNAME, Constants.PASSWORD)
                        result = Result.failure<String>()
                    } else {
                        result = Result.success("success")
                    }
                }
        return result!!
    }

    override fun register(tel: String, password: String): Result<String> {
        var task = Restful.instance().register(tel, password)
        TaskDriver.instance().execute(task)
        var result: Result<String>? = Result.failure()
        task.get()
                .ifFailedSendTo { Result.failure<String>(it) }
                .ifSucceededSendTo {
                    if (it.responseCode > 400) {
                        result = Result.failure<String>()
                    } else if (it.responseCode in 201..300) {
                        result = Result.success("success")
                    }
                }
        return result!!
    }
}