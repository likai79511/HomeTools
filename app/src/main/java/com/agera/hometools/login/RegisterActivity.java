package com.agera.hometools.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.agera.hometools.R;
import com.google.android.agera.BaseObservable;
import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Result;
import com.google.android.agera.Updatable;
import com.google.android.agera.net.HttpResponse;

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
        String s = "123";
        if (TextUtils.isEmpty(s) || s.length()<=10){

        }
        mOb = new OnClickListenerObservable();
        mBtn_register.setOnClickListener(mOb);
        mRep = Repositories.repositoryWithInitialValue(Result.<HttpResponse>absent())
                .observe(mOb)
                .onUpdatesPerLoop()
                .check(o -> activeOnce.getAndSet(true))
                .orSkip()
                .getFrom(() -> {
                    return tel = mEt_tel == null ? null : mEt_tel.getText() == null ? null : mEt_tel.getText().toString().trim();
                })
                .check(tel->LoginFunctionsImp.instance().checkTel(mEt_tel).apply(tel))
                .orSkip()
                .getFrom(() -> {
                    return password = mEt_password == null ? null : mEt_password.getText() == null ? null : mEt_password.getText().toString().trim();
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
                .notifyIf((response1,response2)-> LoginFunctionsImp.instance().checkRegister(mBtn_register).apply(response2))
                .compile();
        activeOnce.set(false);
        mRep.addUpdatable(this);

        findViewById(R.id.btn_login).setOnClickListener(l->{
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        });
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
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }

    class OnClickListenerObservable extends BaseObservable implements Button.OnClickListener {
        @Override
        public void onClick(View v) {
            dispatchUpdate();
        }
    }


}
