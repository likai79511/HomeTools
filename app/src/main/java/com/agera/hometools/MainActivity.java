package com.agera.hometools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.agera.hometools.locate.LocateActivity;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_locate).setOnClickListener(this);
        findViewById(R.id.btn_search_way).setOnClickListener(this);
        findViewById(R.id.btn_family).setOnClickListener(this);
        findViewById(R.id.btn_kd).setOnClickListener(this);
        findViewById(R.id.btn_setting).setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_locate:
                startActivity(new Intent(this, LocateActivity.class));
                break;
            case R.id.btn_search_way:
                break;
            case R.id.btn_family:
                break;
            case R.id.btn_kd:
                break;
            case R.id.btn_setting:
                break;
        }
    }
}
