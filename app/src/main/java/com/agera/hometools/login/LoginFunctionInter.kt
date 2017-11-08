package com.agera.hometools.login

import android.util.Pair
import android.view.View
import com.agera.hometools.network.Callback
import com.google.android.agera.Function
import com.google.android.agera.Predicate
import com.google.android.agera.Result
import com.google.android.agera.net.HttpResponse

/**
 * Created by  Agera K  on 2017/9/29.
 * As a Factory that Login and Register,provide related Function.
 */
interface LoginFunctionInter {
    /**
     * Check Telphone Function
     */
    fun checkTel(view: View): Predicate<String>?

    /**
     * Check password Function
     */
    fun checkPassword(view: View): Predicate<String>?

    /**
     * Check confirm password Function
     */
    fun checkConfirmPassword(view: View): Predicate<String>?

    /**
     * Register
     *          send a request to register
     * Note: These are two ways to get Http-Response data
     *       If callback is null, will use the observer{@link com.google.android.agera.Updatable} to pull result from  Repository by manually.
     *       If callback is no-null,The futuretask will handle result by self.
     */
    fun register(callback: Callback, view: View): Function<Pair<String, String>, Result<HttpResponse>>?

    /**
     * Check Register
     *      According the response to check wether success
     * @return
     */
    fun checkRegister(view: View):Predicate<Result<HttpResponse>>?

    /**
     * Login
     *      send a request to login
    Note: These are two ways to get Http-Response data
     *       If callback is null, will use the observer{@link com.google.android.agera.Updatable} to pull result from  Repository by manually.
     *       If callback is no-null,The futuretask will handle result by self.
     */
    fun login(callback: Callback, view: View):Function<Pair<String,String>,Result<HttpResponse>>?

    /**
     * Check Login
     *      According the response to check wether success
     * @return
     */
    fun checkLogin(view: View):Predicate<Result<HttpResponse>>?

}