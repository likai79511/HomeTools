package com.agera.hometools.login;

import android.util.Pair;
import android.view.View;

import com.agera.hometools.network.Callback;
import com.google.android.agera.Binder;
import com.google.android.agera.Function;
import com.google.android.agera.Predicate;
import com.google.android.agera.Result;
import com.google.android.agera.net.HttpResponse;

/**
 * Created by  Agera K  on 2017/9/29.
 * As a Factory that Login and Register,provide related Function.
 */
public interface LoginFunctionInter {
    /**
     * Check Telphone Function
     */
    Predicate<String> checkTel(View view);

    /**
     * Check password Function
     */
    Predicate<String> checkPassword(View view);

    /**
     * Check confirm password Function
     */
    Predicate<String> checkConfirmPassword(String password,View view);

    /**
     * Register
     *          send a request to register
     * Note: These are two ways to get Http-Response data
     *       If callback is null, will use the observer{@link com.google.android.agera.Updatable} to pull result from  Repository by manually.
     *       If callback is no-null,The futuretask will handle result by self.
     */
    Function<Pair<String,String>, Result<HttpResponse>> register(Callback callback,View view);

    /**
     * Check Register
     *      According the response to check wether success
     * @return
     */
    Predicate<Result<HttpResponse>> checkRegister(View view);

}
