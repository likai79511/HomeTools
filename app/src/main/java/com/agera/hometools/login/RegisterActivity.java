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
import com.google.android.agera.BaseObservable;
import com.google.android.agera.Function;
import com.google.android.agera.Merger;
import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Result;
import com.google.android.agera.Supplier;
import com.google.android.agera.Updatable;

public class RegisterActivity extends Activity implements Updatable {
    private boolean flag = false;
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
                        Log.e("---", "--getFrom-001 -" + Thread.currentThread().getId() + "--tel:" + tel);
                        return tel;
                    }
                })
                .attemptTransform(LoginFunctionsImp.instance().checkTel())      //telephone number
                .orEnd(LoginFunctionsImp.instance().handleError(mEt_tel))              //handle number error
//                .goTo(TaskDriver.instance().getThreadPool())
                .getFrom(new Supplier<String>() {                            //get password
                    @NonNull
                    @Override
                    public String get() {
                        Log.e("---","--getFrom-02"+Thread.currentThread().getId());
                        password = mEt_password == null ? null : mEt_password.getText() == null ? null : mEt_password.getText().toString().trim();
                        return password;
                    }
                })
                .attemptTransform(LoginFunctionsImp.instance().checkPassword())    //check password
                .orEnd(LoginFunctionsImp.instance().handleError(mEt_password))                 //hanlde password  error
                .getFrom(new Supplier<String>() {                               //get confirm password
                    @NonNull
                    @Override
                    public String get() {
                        Log.e("---","--getFrom-03"+Thread.currentThread().getId());
                        confirm_password = mEt_confirm_password == null ? null : mEt_confirm_password.getText() == null ? null : mEt_confirm_password.getText().toString().trim();
                        return confirm_password;
                    }
                })
                .attemptTransform(LoginFunctionsImp.instance().checkConfirmPassword(password))
                .orEnd(LoginFunctionsImp.instance().handleError(mEt_confirm_password))
                .transform(new Function<String, Pair<String, String>>() {
                    @NonNull
                    @Override
                    public Pair<String, String> apply(@NonNull String input) {
                        return Pair.create(tel, password);
                    }
                })
                .thenTransform(LoginFunctionsImp.instance().register(null))
                .notifyIf(new Merger<Object, Object, Boolean>() {
                    @NonNull
                    @Override
                    public Boolean merge(@NonNull Object o, @NonNull Object o2) {
                        Log.e("---","---notifyIf:o:"+o+"---o2:"+o2);
                        return o!=null;
                    }
                })
                .compile();
    }

    private void initViews() {
        mEt_tel = (EditText) findViewById(R.id.et_tel);
        mEt_password = (EditText) findViewById(R.id.et_password);
        mEt_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void update() {


    }

    class OnClickListenerObservable extends BaseObservable implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            dispatchUpdate();
            mRep.get();
        }
    }
}
