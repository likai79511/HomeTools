package com.agera.hometools.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.agera.hometools.R;
import com.google.android.agera.Function;
import com.google.android.agera.Repositories;
import com.google.android.agera.Result;
import com.google.android.agera.Supplier;

public class RegisterActivity extends Activity {

    private EditText mEt_tel = null;
    private EditText mEt_password = null;
    private EditText mEt_confirm_password = null;
    private String tel = null;
    private String password = null;
    private String confirm_password= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initEvents();
    }

    private void initEvents() {
        Repositories.repositoryWithInitialValue(null)
                .observe()
                .onUpdatesPerLoop()
                .attemptGetFrom(new Supplier<Result<String>>() {
                    @NonNull
                    @Override
                    public Result<String> get() {
                        tel = mEt_tel.getText().toString().trim();
                        if (tel != null || tel.length()==11)
                            return Result.success(tel);
                        return Result.failure(new Exception("please input correct telephone."));
                    }
                })
                .orEnd(new Function<Throwable, Object>() {
                    @NonNull
                    @Override
                    public Object apply(@NonNull Throwable input) {
                        Snack
                        return null;
                    }
                })
                .attemptGetFrom()


    }

    private void initViews() {
        mEt_tel = (EditText) findViewById(R.id.et_tel);
        mEt_password = (EditText) findViewById(R.id.et_password);
        mEt_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
    }
}
