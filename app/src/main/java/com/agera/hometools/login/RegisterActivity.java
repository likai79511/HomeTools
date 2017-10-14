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
import com.google.android.agera.Receiver;
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
        mRep = Repositories.repositoryWithInitialValue(Result.<HttpResponse>absent())
                .observe(mOb)
                .onUpdatesPerLoop()
                .check(o -> activeOnce.getAndSet(true))
                .orSkip()
                .getFrom(() -> {
                    tel = mEt_tel == null ? null : mEt_tel.getText() == null ? null : mEt_tel.getText().toString().trim();
                    return tel;
                })
                .attemptTransform(s->LoginFunctionsImp.instance().checkTel().apply(s))
                .orEnd(e->LoginFunctionsImp.instance().handleError(mEt_tel).apply(e))
                .getFrom(() -> {
                    password = mEt_password == null ? null : mEt_password.getText() == null ? null : mEt_password.getText().toString().trim();
                    return password;
                })
                .attemptTransform(s->LoginFunctionsImp.instance().checkPassword().apply(s))
                .orEnd(e->LoginFunctionsImp.instance().handleError(mEt_password).apply(e))
                .getFrom(() -> {
                    confirm_password = mEt_confirm_password == null ? null : mEt_confirm_password.getText() == null ? null : mEt_confirm_password.getText().toString().trim();
                    return confirm_password;
                })
                .attemptTransform(s->LoginFunctionsImp.instance().checkConfirmPassword(password).apply(s))
                .orEnd(e->LoginFunctionsImp.instance().handleError(mEt_confirm_password).apply(e))
                .transform(s -> Pair.create(tel, password))
                .attemptTransform(s->LoginFunctionsImp.instance().register(null).apply(s))
                .orSkip()
                .thenSkip()
                //.s->LoginFunctionsImp.instance().register(null).apply(s)
                .notifyIf((o1, o2) -> o1 != null)
                .compile();
        activeOnce.set(false);
        mRep.addUpdatable(this);
    }

    private void initViews() {
        mEt_tel = (EditText) findViewById(R.id.et_tel);
        mEt_password = (EditText) findViewById(R.id.et_password);
        mEt_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRep.removeUpdatable(this);
    }

    @Override
    public void update() {
        mRep.get()
                .ifSucceededSendTo(new Receiver<HttpResponse>() {
                    @Override
                    public void accept(@NonNull HttpResponse value) {
                        Log.e("---", "--result:success\n" + value);
                    }
                })
                .ifFailedSendTo(new Receiver<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable value) {
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
