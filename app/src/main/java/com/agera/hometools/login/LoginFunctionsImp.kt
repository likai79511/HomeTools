package com.agera.hometools.login

import android.content.Context
import android.text.TextUtils
import android.util.Pair
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.agera.hometools.MyApp
import com.agera.hometools.bean.LoginResponse
import com.agera.hometools.core.HttpTask
import com.agera.hometools.core.TaskDriver
import com.agera.hometools.network.Callback
import com.agera.hometools.network.Restful
import com.agera.hometools.utils.CommonUtils
import com.agera.hometools.utils.Constants
import com.google.android.agera.Function
import com.google.android.agera.Predicate
import com.google.android.agera.Result
import com.google.android.agera.net.HttpResponse
import java.lang.ref.WeakReference

/**
 * Created by  Agera K on 2017/9/29.
 * As a Factory that Login and Register,provide related Function.
 */
class LoginFunctionsImp private constructor() : LoginFunctionInter {

    companion object {
        private val instance: LoginFunctionsImp = LoginFunctionsImp()
        fun instance(): LoginFunctionsImp {
            return instance
        }
    }

    /**
     * Check Telphone Function
     */
    override fun checkTel(view: View): Predicate<String>? {
        return WeakReference<Predicate<String>>(Predicate {
            (MyApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
            if (TextUtils.isEmpty(it) || it.length != 11) {
                CommonUtils.instance().showShortMessage(view, "telephone lenth is wrong")
                false
            }
            true
        }).get()
    }

    /**
     * Check password Function
     */
    override fun checkPassword(view: View): Predicate<String>? {
        return WeakReference<Predicate<String>>(Predicate {
            (MyApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
            if (TextUtils.isEmpty(it) || it.length <= 6) {
                CommonUtils.instance().showShortMessage(view, "password length must is 6~11")
                false
            }
            true
        }).get()
    }

    /**
     * Check confirm password Function
     */
    override fun checkConfirmPassword(view: View): Predicate<String>? {
        return WeakReference<Predicate<String>>(Predicate {
            (MyApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
            if (TextUtils.isEmpty(it) || it.length <= 6) {
                CommonUtils.instance().showShortMessage(view, "两次密码必须一致")
                false
            }
            true
        }).get()
    }

    /**
     * Register
     */
    override fun register(callback: Callback, view: View): Function<Pair<String, String>, Result<HttpResponse>>? {
        return WeakReference<Function<Pair<String, String>, Result<HttpResponse>>>(Function {
            try {
                (MyApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
                CommonUtils.instance().showShortMessage(view, "正在注册...")
                view.isClickable = false
                var task: HttpTask = Restful.register(it.first, it.second, callback)
                TaskDriver.instance().execute(task)
                task.get()
            } catch (e: Exception) {
                if (callback != null)
                    callback.error(e)
                Result.failure(e)
            }
        }).get()
    }

    /**
     * Check Register
     */
    override fun checkRegister(view: View): Predicate<Result<HttpResponse>>? {
        return WeakReference<Predicate<Result<HttpResponse>>>(
                Predicate<Result<HttpResponse>> {
                    view.isClickable = true
                    if (it.failed() || it.get().responseCode > 400) {
                        CommonUtils.instance().showShortMessage(view, "注册失败,该手机号已被使用")
                        false
                    }
                    true
                }
        ).get()
    }

    /**
     * Login
     */
    override fun login(callback: Callback, view: View): Function<Pair<String, String>, Result<HttpResponse>>? {
        return WeakReference<Function<Pair<String, String>, Result<HttpResponse>>>(Function {
            try {
                (MyApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
                CommonUtils.instance().showShortMessage(view, "正在登陆...")
                var task: HttpTask = Restful.login(it.first, it.second, callback)
                TaskDriver.instance().execute(task)
                task.get()
            } catch (e: Exception) {
                if (callback != null)
                    callback.error(e)
                view.isClickable = true
                Result.failure(e)
            }
        }).get()
    }

    /**
     * Check Login
     */
    override fun checkLogin(view: View): Predicate<Result<HttpResponse>>? {
        return WeakReference<Predicate<Result<HttpResponse>>>(Predicate {
            try {
                (MyApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
                view.isClickable = true
                if (it.failed() || it.get().responseCode > 400 || CommonUtils.gson.fromJson(it.get().bodyString.get(), LoginResponse:class.java).getResults().size<1){
                    CommonUtils.instance().showShortMessage(view, "登录失败，账号密码有误")
                    CommonUtils.instance().clearData(Constants.USERNAME, Constants.PASSWORD)
                    false
                }
                true
            } catch (e: Exception) {
                CommonUtils.instance().showShortMessage(view, "登录失败，账号密码有误")
                CommonUtils.instance().clearData(Constants.USERNAME, Constants.PASSWORD)
                false
            }
        }).get()
    }
}