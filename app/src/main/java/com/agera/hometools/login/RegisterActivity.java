package com.agera.hometools.login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.agera.hometools.MyApp;
import com.agera.hometools.R;
import com.agera.hometools.core.TaskDriver;
import com.google.android.agera.BaseObservable;
import com.google.android.agera.Function;
import com.google.android.agera.Merger;
import com.google.android.agera.Receiver;
import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Result;
import com.google.android.agera.Updatable;
import com.google.android.agera.net.HttpResponse;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicBoolean;

public class RegisterActivity extends Activity implements Updatable {
    private EditText mEt_tel = null;
    private EditText mEt_password = null;
    private EditText mEt_confirm_password = null;
    private String tel = null;
    private String password = null;
    private String confirm_password = null;
    private Repository<Result<HttpResponse>> mRep = null;
    private OnClickListenerObservable mOb = null;
    private AtomicBoolean activeOnce = new AtomicBoolean(false);
    private Button mBtn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initEvents();
    }

    private void initEvents() {
        Log.e("---","---main-threadID:"+Thread.currentThread().getId());
        mOb = new OnClickListenerObservable();
        mBtn_register.setOnClickListener(mOb);
        mRep = Repositories.repositoryWithInitialValue(Result.<HttpResponse>absent())
                .observe(mOb)
                .onUpdatesPerLoop()
                .check(o -> activeOnce.getAndSet(true))
                .orSkip()
                .getFrom(() -> {
                    tel = mEt_tel == null ? null : mEt_tel.getText() == null ? null : mEt_tel.getText().toString().trim();
                    return tel;
                })
                .check(tel->LoginFunctionsImp.instance().checkTel(mEt_tel).apply(tel))
                .orSkip()
                .getFrom(() -> {
                    password = mEt_password == null ? null : mEt_password.getText() == null ? null : mEt_password.getText().toString().trim();
                    return password;
                })
                .check(password->LoginFunctionsImp.instance().checkPassword(mEt_password).apply(password))
                .orSkip()
                .getFrom(() -> {
                    confirm_password = mEt_confirm_password == null ? null : mEt_confirm_password.getText() == null ? null : mEt_confirm_password.getText().toString().trim();
                    return confirm_password;
                })
                .check(confirm_password -> LoginFunctionsImp.instance().checkConfirmPassword(password,mEt_confirm_password).apply(confirm_password))
                .orSkip()
                .thenTransform(s -> LoginFunctionsImp.instance().register(null,mBtn_register).apply(Pair.create(tel, password)))
                .notifyIf(new Merger<Result<HttpResponse>, Result<HttpResponse>, Boolean>() {
                    @NonNull
                    @Override
                    public Boolean merge(@NonNull Result<HttpResponse> httpResponseResult, @NonNull Result<HttpResponse> httpResponseResult2) {
                        Log.e("---","---notifyIf--");
                        Log.e("---","---notifyIf--httpResponseResult:"+httpResponseResult);
                        if (!httpResponseResult.isAbsent()){
                            Log.e("---","---notifyIf--httpResponseResult.get: "+httpResponseResult.get());
                        }
                        Log.e("---","---notifyIf--httpResponseResult2:"+httpResponseResult2);
                        if (httpResponseResult2!=null && !httpResponseResult2.isAbsent() && httpResponseResult2.succeeded()){
                            Log.e("---","---notifyIf--httpResponseResult2.get:"+httpResponseResult2.get());
                        }
                        return  LoginFunctionsImp.instance().checkRegister(mBtn_register).apply(httpResponseResult2);
                    }
                })
//                .check(result->LoginFunctionsImp.instance().checkRegister(mBtn_register).apply(result))
                .compile();
        activeOnce.set(false);
        mRep.addUpdatable(this);
    }

    private void initViews() {
        mEt_tel = (EditText) findViewById(R.id.et_tel);
        mEt_password = (EditText) findViewById(R.id.et_password);
        mEt_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
        mBtn_register = (Button) findViewById(R.id.btn_register);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRep.removeUpdatable(this);
    }

    @Override
    public void update() {
        Log.e("---", "---update--:"+Thread.currentThread().getId());
        ((InputMethodManager) MyApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        mRep.get()
                .ifSucceededSendTo(new Receiver<HttpResponse>() {
                    @Override
                    public void accept(@NonNull HttpResponse value) {
                        try {
                            Log.e("---","---threadID:"+Thread.currentThread().getId());
                            Log.e("---", "--result:success\n" + value + "\nmsg:" + value.getResponseMessage() + "\nbody:" + value.getBodyString().get() + "\nbody02:" + new String(value.getBody(), "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            Log.e("---", "----occuer error:" + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                })
                .ifFailedSendTo(new Receiver<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable value) {
                        Log.e("---","---threadID:"+Thread.currentThread().getId());
                        Log.e("---", "---update:failed\n" + value.getMessage());
                    }
                });
    }

    class OnClickListenerObservable extends BaseObservable implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            dispatchUpdate();
        }
    }


}
