package com.agera.hometools.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.agera.hometools.R;
import com.agera.hometools.network.Callback;
import com.google.android.agera.BaseObservable;
import com.google.android.agera.Function;
import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Supplier;
import com.google.android.agera.net.HttpResponse;

public class RegisterActivity extends Activity {

    private EditText mEt_tel = null;
    private EditText mEt_password = null;
    private EditText mEt_confirm_password = null;
    private String tel = null;
    private String password = null;
    private String confirm_password = null;
    private Repository mRep = null;
    private OnClickListenerObservable mOb = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initEvents();
    }

    private void initEvents() {
        mOb = new OnClickListenerObservable();
        findViewById(R.id.btn_register).setOnClickListener(mOb);
        mRep = Repositories.repositoryWithInitialValue(null)
                .observe(mOb)
                .onUpdatesPerLoop()
                .getFrom(new Supplier<String>() {                           //get telephone number
                    @NonNull
                    @Override
                    public String get() {
                        tel = mEt_tel == null ? null : mEt_tel.getText() == null ? null : mEt_tel.getText().toString().trim();
                        return tel;
                    }
                })
                .attemptTransform(LoginFunctionsImp.instance().checkTel())      //telephone number
                .orEnd(LoginFunctionsImp.instance().handleError())              //handle number error
                .getFrom(new Supplier<String>() {                            //get password
                    @NonNull
                    @Override
                    public String get() {
                        password = mEt_password == null ? null : mEt_password.getText() == null ? null : mEt_password.getText().toString().trim();
                        return password;
                    }
                })
                .attemptTransform(LoginFunctionsImp.instance().checkPassword())    //check password
                .orEnd(LoginFunctionsImp.instance().handleError())                 //hanlde password  error
                .getFrom(new Supplier<String>() {                               //get confirm password
                    @NonNull
                    @Override
                    public String get() {
                        confirm_password = mEt_confirm_password == null ? null : mEt_confirm_password.getText() == null ? null : mEt_confirm_password.getText().toString().trim();
                        return confirm_password;
                    }
                })
                .attemptTransform(LoginFunctionsImp.instance().checkConfirmPassword(password))
                .orEnd(LoginFunctionsImp.instance().handleError())
                .transform(new Function<String, Pair<String, String>>() {
                    @NonNull
                    @Override
                    public Pair<String, String> apply(@NonNull String input) {
                        return Pair.create(tel, password);
                    }
                })
                .thenAttemptTransform(LoginFunctionsImp.instance().register(new Callback() {
                    @Override
                    public void success(HttpResponse result) {
                        Log.e("---", "---success:" + Thread.currentThread().getId() + "\n" + result.getResponseMessage());
                    }

                    @Override
                    public void error(Throwable e) {
                        super.error(new Exception("register failed:"+Thread.currentThread().getId() + "\n" + e.getMessage()));
                    }
                }))
                .orEnd(LoginFunctionsImp.instance().handleError())
                .compile();
    }

    private void initViews() {
        mEt_tel = (EditText) findViewById(R.id.et_tel);
        mEt_password = (EditText) findViewById(R.id.et_password);
        mEt_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
    }

    class OnClickListenerObservable extends BaseObservable implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.e("---","---dispatchUpdate");
            dispatchUpdate();
        }
    }
}
