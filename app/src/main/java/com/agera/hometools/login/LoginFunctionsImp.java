package com.agera.hometools.login;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Pair;
import android.widget.Toast;

import com.agera.hometools.MyApp;
import com.agera.hometools.core.TaskDriver;
import com.agera.hometools.network.Callback;
import com.agera.hometools.network.Restful;
import com.google.android.agera.Function;
import com.google.android.agera.Result;

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
        WeakReference<Function<String, Result<String>>> weakReference = new WeakReference<Function<String, Result<String>>>(new Function<String, Result<String>>() {
            @NonNull
            @Override
            public Result<String> apply(@NonNull String input) {
                if (TextUtils.isEmpty(input) || input.length() != 11)
                    return Result.failure(new Exception("telephone lenth is wrong"));
                return Result.success(input);
            }
        });
        return weakReference.get();
    }

    /**
     * Check password Function
     */
    @Override
    public Function<String, Result<String>> checkPassword() {
        WeakReference<Function<String, Result<String>>> weakReference = new WeakReference<Function<String, Result<String>>>(new Function<String, Result<String>>() {
            @NonNull
            @Override
            public Result<String> apply(@NonNull String input) {
                if (TextUtils.isEmpty(input) || input.length() <= 6)
                    return Result.failure(new Exception("password length must is 6~11"));
                return Result.success(input);
            }
        });
        return weakReference.get();
    }

    /**
     * Check confirm password Function
     */
    @Override
    public Function<String, Result<String>> checkConfirmPassword(final String password) {
        WeakReference<Function<String, Result<String>>> weakReference = new WeakReference<Function<String, Result<String>>>(new Function<String, Result<String>>() {
            @NonNull
            @Override
            public Result<String> apply(@NonNull String input) {
                if (password.equals(input))
                    return Result.success(input);
                return Result.failure(new Exception("The two passwords must be consistent"));
            }
        });
        return weakReference.get();
    }


    /**
     * Error handle
     */
    @Override
    public Function<Throwable, Object> handleError() {
        WeakReference<Function<Throwable, Object>> weakReference = new WeakReference<Function<Throwable, Object>>(new Function<Throwable, Object>() {
            @NonNull
            @Override
            public Object apply(@NonNull Throwable input) {
                Toast.makeText(MyApp.getInstance(), input.getMessage(), Toast.LENGTH_SHORT).show();
                return null;
            }
        });
        return weakReference.get();
    }

    @Override
    public Function<Pair<String, String>, Result<String>> register(final Callback callback) {
        WeakReference<Function<Pair<String, String>, Result<String>>> weakReference = new WeakReference<Function<Pair<String, String>, Result<String>>>(new Function<Pair<String, String>, Result<String>>() {
            @NonNull
            @Override
            public Result<String> apply(@NonNull Pair<String, String> input) {
                try {
                    TaskDriver.instance().execute(Restful.register(input.first, input.second, callback));
                    return Result.success("");
                } catch (Exception e) {
                    return Result.failure(e);
                }
            }
        });
        return weakReference.get();
    }
}
