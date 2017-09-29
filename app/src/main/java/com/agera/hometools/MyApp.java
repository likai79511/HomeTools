package com.agera.hometools;

import android.app.Application;

/**
 * Created by 43992639 on 2017/9/29.
 */
public class MyApp extends Application {

    private static MyApp instance = null;

    public static MyApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
