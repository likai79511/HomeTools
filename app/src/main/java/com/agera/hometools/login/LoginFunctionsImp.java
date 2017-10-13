package com.agera.hometools.login;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.agera.hometools.MyApp;
import com.agera.hometools.core.HttpTask;
import com.agera.hometools.core.TaskDriver;
import com.agera.hometools.network.Callback;
import com.agera.hometools.network.Restful;
import com.google.android.agera.Function;
import com.google.android.agera.Result;
import com.google.android.agera.net.HttpResponse;

import java.lang.ref.WeakReference;

/**
 * Created by  Agera K on 2017/9/29.
 * As a Factory that Login and Register,provide related Function.
 */
public class LoginFunctionsImp implements LoginFunctionInter {

    private LoginFunctionsImp() {
    }

    private static LoginFunctionsImp instance = null;

    public static LoginFunctionsImp instance() {
        synchronized (LoginFunctionsImp.class) {
            if (instance == null)
                instance = new LoginFunctionsImp();
        }
        return instance;
    }

    /**
     * Check Telphone Function
     */
    @Override
    public Function<String, Result<String>> checkTel() {
        Log.e("--","---checkTel--01");
        WeakReference<Function<String, Result<String>>> weakReference = new WeakReference<Function<String, Result<String>>>(s->{
            if (TextUtils.isEmpty(s) || s.length() != 11)
                return Result.failure(new Exception("telephone lenth is wrong"));
            return Result.success(s);
        });
        return weakReference.get();
    }

    /**
     * Check password Function
     */
    @Override
    public Function<String, Result<String>> checkPassword() {
        Log.e("--","---checkPassword--01");
        WeakReference<Function<String, Result<String>>> weakReference = new WeakReference<Function<String, Result<String>>>(input->{
            if (TextUtils.isEmpty(input) || input.length() <= 6)
                return Result.failure(new Exception("password length must is 6~11"));
            return Result.success(input);
        });
        return weakReference.get();
    }

    /**
     * Check confirm password Function
     */
    @Override
    public Function<String, Result<String>> checkConfirmPassword(final String password) {
        Log.e("--","---checkConfirmPassword--01");
        WeakReference<Function<String, Result<String>>> weakReference = new WeakReference<Function<String, Result<String>>>(input->{
            if (password.equals(input))
                return Result.success(input);
            return Result.failure(new Exception("The two passwords must be consistent"));
        });
        return weakReference.get();
    }


    /**
     * Error handle
     */
    @Override
    public Function<Throwable, Result<HttpResponse>> handleError(final View view) {
        Log.e("--","---handleError--01");
        WeakReference<Function<Throwable, Result<HttpResponse>>> weakReference = new WeakReference<Function<Throwable, Result<HttpResponse>>>(input->{
            ((InputMethodManager) MyApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
            if (view == null) {
                Toast.makeText(MyApp.getInstance(), input.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Snackbar.make(view, input.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
            return Result.<HttpResponse>absent();
        });
        return weakReference.get();
    }


    @Override
    public Function<Pair<String, String>, Result<HttpResponse>> register(final Callback cb) {
        Log.e("--","---register--01");
        final WeakReference<Function<Pair<String, String>, Result<HttpResponse>>> weakReference = new WeakReference<Function<Pair<String, String>, Result<HttpResponse>>>(input->{
            try {
                HttpTask task = Restful.register(input.first, input.second, cb);
                TaskDriver.instance().execute(task);
                return task.get();
            } catch (Exception e) {
                if (cb != null)
                    cb.error(e);
                return Result.failure(e);
            }
        });
        return weakReference.get();
    }
}
