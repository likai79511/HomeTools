package com.agera.hometools.login;

import android.util.Pair;

import com.agera.hometools.network.Callback;
import com.google.android.agera.Binder;
import com.google.android.agera.Function;
import com.google.android.agera.Result;

/**
 * Created by  Agera K  on 2017/9/29.
 */
public interface LoginFunctionInter {
    /**
     * Check Telphone Function
     */
    Function<String, Result<String>> checkTel();

    /**
     * Check password Function
     */
    Function<String, Result<String>> checkPassword();


    /**
     * Check confirm password Function
     */
    Function<String, Result<String>> checkConfirmPassword(final String password);

    /**
     * Error handle
     */
    Function<Throwable, Object> handleError();

    /**
     * Register
     */
    Function<Pair<String,String>, Result<String>> register(Callback callback);
}
