package com.agera.hometools.login;

import android.util.Pair;
import android.view.View;

import com.agera.hometools.network.Callback;
import com.google.android.agera.Binder;
import com.google.android.agera.Function;
import com.google.android.agera.Result;
import com.google.android.agera.net.HttpResponse;

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
    Function<Throwable, Result<HttpResponse>> handleError(View view);

    /**
     * Register
     * Note: These are two ways to get Http-Response data
     *       If callback is null, will use the observer{@link com.google.android.agera.Updatable} to pull result from  Repository by manually.
     *       If callback is no-null,The futuretask will handle result by self.
     */
    Function<Pair<String,String>, Result<HttpResponse>> register(Callback callback);
}
