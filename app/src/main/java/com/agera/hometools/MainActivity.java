package com.agera.hometools;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.agera.hometools.core.HttpCallable;
import com.agera.hometools.core.HttpTask;
import com.agera.hometools.core.TaskDriver;
import com.google.android.agera.net.HttpRequest;
import com.google.android.agera.net.HttpRequests;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void test(View view){
        Map<String,String> map = new HashMap<>();
//        map.put("objectId","001");
        map.put("name","Agera01");
        map.put("password","1234");
        Gson gson = new Gson();
        String data = gson.toJson(map);
        HttpRequest request = HttpRequests.httpPostRequest("https://api.bmob.cn/1/classes/user")
                .body(data.getBytes())
                .headerField("X-Bmob-Application-Id","fc50317a2f1dc14576f7c600697e6799")
                .headerField("X-Bmob-REST-API-Key","51c117839a06c75aa380eb78eb8395b7")
                .headerField("Content-Type","application/json")
                .compile();
        TaskDriver.instance().execute(new HttpTask(new HttpCallable(request)));
    }
}
