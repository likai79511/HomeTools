package com.agera.hometools.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.agera.hometools.MainActivity;
import com.agera.hometools.R;
import com.google.android.agera.BaseObservable;
import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Result;
import com.google.android.agera.Updatable;
import com.google.android.agera.net.HttpResponse;

import java.util.concurrent.atomic.AtomicBoolean;

public class LoginActivity extends Activity implements Updatable {

    private EditText mEt_tel = null;
    private EditText mEt_password = null;
    private Button mBtn_login = null;
    private OnClickListenerObservable mOb = null;
    private Repository<Result<HttpResponse>> mRep = null;
    private String tel = null;
    private String password = null;
    private AtomicBoolean activeOnce = new AtomicBoolean(false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initEvents();
    }

    private void initEvents() {
        mOb = new OnClickListenerObservable();
        mBtn_login.setOnClickListener(mOb);
        mRep = Repositories.repositoryWithInitialValue(Result.<HttpResponse>absent())
                .observe(mOb)
                .onUpdatesPerLoop()
                .check(o -> activeOnce.getAndSet(true))
                .orSkip()
                .getFrom(() -> {
                    return tel = mEt_tel == null ? null : mEt_tel.getText() == null ? null : mEt_tel.getText().toString().trim();
                })
                .check(s -> LoginFunctionsImp.instance().checkTel(mEt_tel).apply(s))
                .orSkip()
                .getFrom(() -> {
                    return password = mEt_password == null ? null : mEt_password.getText() == null ? null : mEt_password.getText().toString().trim();
                })
                .check(s -> LoginFunctionsImp.instance().checkPassword(mEt_password).apply(s))
                .orSkip()
                .thenTransform(s -> LoginFunctionsImp.instance().login(null, mBtn_login).apply(Pair.create(tel, password)))
                .notifyIf((response1, response2) -> LoginFunctionsImp.instance().checkLogin(mBtn_login).apply(response2))
                .compile();
        activeOnce.set(false);
        mRep.addUpdatable(this);
    }

    private void initView() {
        mEt_tel = (EditText) findViewById(R.id.et_tel);
        mEt_password = (EditText) findViewById(R.id.et_password);
        mBtn_login = (Button) findViewById(R.id.btn_Login);
    }

    @Override
    public void update() {
        Log.e("---", "----logon success");
        startActivity(new Intent(this, MainActivity.class));
    }

    class OnClickListenerObservable extends BaseObservable implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            dispatchUpdate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRep.removeUpdatable(this);
    }
}
