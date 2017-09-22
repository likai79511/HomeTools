package com.agera.hometools;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.agera.hometools.core.TaskDriver;
import com.agera.hometools.network.Callback;
import com.agera.hometools.network.Restful;
import com.google.android.agera.net.HttpResponse;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void test(View view){
    TaskDriver.instance().execute(Restful.register("wahaha001", "1234567", new Callback() {
        @Override
        public void call(HttpResponse result) {
            Log.e("---","--result:"+result.getResponseMessage());
        }

        @Override
        public void error(Throwable e) {
            Log.e("---","--error:"+e.getMessage());
        }
    }));
    }
}
