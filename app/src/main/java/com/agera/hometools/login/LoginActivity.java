package com.agera.hometools.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.agera.hometools.R;
import com.google.android.agera.BaseObservable;
import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Updatable;

public class LoginActivity extends Activity implements Updatable{

    private EditText mEt_tel = null;
    private EditText mEt_password = null;
    private OnClickListenerObservable mOb = null;
    private Repository<String> mRep = null;
    private String tel= null;
    private String password= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initEvents();
    }

    private void initEvents() {
        mOb = new OnClickListenerObservable();
        mRep = Repositories.repositoryWithInitialValue("")
                .observe(mOb)
                .onUpdatesPerLoop()
                .getFrom(()->{
                    return tel = mEt_tel == null ? null : mEt_tel.getText() == null ? null : mEt_tel.getText().toString().trim();
                })
                .check(s->LoginFunctionsImp.instance().checkTel(mEt_tel).apply(s))
                .orSkip()
                .getFrom(()->{
                    return password = mEt_password == null ? null : mEt_password.getText() == null ? null : mEt_password.getText().toString().trim();
                })
                .check(s->LoginFunctionsImp.instance().checkPassword(mEt_password).apply(s))
                .orSkip()
                .thenTransform()


    }

    private void initView() {
        mEt_tel = (EditText) findViewById(R.id.et_tel);
        mEt_password = (EditText) findViewById(R.id.et_password);
    }

    @Override
    public void update() {

    }

    class OnClickListenerObservable extends BaseObservable implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            dispatchUpdate();
        }
    }
}
