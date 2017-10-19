package com.agera.hometools.login;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.agera.hometools.MyApp;
import com.agera.hometools.bean.LoginResponse;
import com.agera.hometools.core.HttpTask;
import com.agera.hometools.core.TaskDriver;
import com.agera.hometools.network.Callback;
import com.agera.hometools.network.Restful;
import com.agera.hometools.utils.CommonUtils;
import com.google.android.agera.Function;
import com.google.android.agera.Predicate;
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
    public Predicate<String> checkTel(View view) {
        WeakReference<Predicate<String>> weakReference = new WeakReference<Predicate<String>>(s -> {
            ((InputMethodManager) MyApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
            if (TextUtils.isEmpty(s) || s.length() != 11) {
                if (view == null) {
                    Toast.makeText(MyApp.getInstance(), "telephone lenth is wrong", Toast.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(view, "telephone lenth is wrong", Snackbar.LENGTH_SHORT).show();
                }
                return false;
            }
            return true;
        });
        return weakReference.get();
    }

    /**
     * Check password Function
     */
    @Override
    public Predicate<String> checkPassword(View view) {
        WeakReference<Predicate<String>> weakReference = new WeakReference<Predicate<String>>(input -> {
            ((InputMethodManager) MyApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
            if (TextUtils.isEmpty(input) || input.length() <= 6) {
                if (view == null) {
                    Toast.makeText(MyApp.getInstance(), "password length must is 6~11", Toast.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(view, "password length must is 6~11", Snackbar.LENGTH_SHORT).show();
                }
                return false;
            }
            return true;
        });
        return weakReference.get();
    }

    /**
     * Check confirm password Function
     */
    @Override
    public Predicate<String> checkConfirmPassword(final String password, View view) {
        WeakReference<Predicate<String>> weakReference = new WeakReference<Predicate<String>>(input -> {
            ((InputMethodManager) MyApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
            if (password.equals(input))
                return true;
            CommonUtils.instance().showMessage("两次密码必须一致",view,Toast.LENGTH_SHORT);
            return false;
        });
        return weakReference.get();
    }


    @Override
    public Function<Pair<String, String>, Result<HttpResponse>> register(final Callback cb, View view) {
        WeakReference<Function<Pair<String, String>, Result<HttpResponse>>> weakReference = new WeakReference<Function<Pair<String, String>, Result<HttpResponse>>>(input -> {
            try {
                ((InputMethodManager) MyApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
                CommonUtils.instance().showMessage("正在注册...",view,Toast.LENGTH_LONG);
                HttpTask task = Restful.register(input.first, input.second, cb);
                view.setClickable(false);
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


    @Override
    public Predicate<Result<HttpResponse>> checkRegister(View view) {
        WeakReference<Predicate<Result<HttpResponse>>> weakReference = new WeakReference<Predicate<Result<HttpResponse>>>(result -> {
            view.setClickable(true);
            if (result.failed() || result.get().getResponseCode() > 400) {
                CommonUtils.instance().showMessage("注册失败,该手机号已被使用",view,Toast.LENGTH_SHORT);
                return false;
            }
            return true;
        });
        return weakReference.get();
    }


    @Override
    public Function<Pair<String, String>, Result<HttpResponse>> login(Callback cb, View view) {
        WeakReference<Function<Pair<String, String>, Result<HttpResponse>>> weakReference = new WeakReference<Function<Pair<String, String>, Result<HttpResponse>>>(input -> {
            try {
                CommonUtils.instance().showMessage("正在登陆...",view,Snackbar.LENGTH_SHORT);
                Log.e("---","---正在登陆--");
                HttpTask task = Restful.login(input.first, input.second, cb);
                view.setClickable(false);
                TaskDriver.instance().execute(task);
                return task.get();
            } catch (Exception e) {
                if (cb != null)
                    cb.error(e);
                view.setClickable(true);
                return Result.failure(e);
            }
        });
        return weakReference.get();
    }

    @Override
    public Predicate<Result<HttpResponse>> checkLogin(View view) {
        WeakReference<Predicate<Result<HttpResponse>>> weakReference = new WeakReference<Predicate<Result<HttpResponse>>>(result->{
            ((InputMethodManager) MyApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
            view.setClickable(true);
           try {
               if (result.failed() || result.get().getResponseCode() > 400 || CommonUtils.instance().gson.fromJson(result.get().getBodyString().get(), LoginResponse.class).getResults().size() <= 1) {
                   CommonUtils.instance().showMessage("登录失败，账号密码有误", view, Toast.LENGTH_SHORT);
                   return false;
               }
               return true;
           }catch (Exception e){
               Log.e("---", "---exception:" + e.getMessage());
               CommonUtils.instance().showMessage("登录失败，账号密码有误", view, Toast.LENGTH_SHORT);
               return false;
           }
        });
        return weakReference.get();
    }

}
