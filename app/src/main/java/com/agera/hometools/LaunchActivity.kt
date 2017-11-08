package com.agera.hometools

import android.app.Activity
import android.os.Bundle

/**
 * Created by mac on 2017/10/23.
 */
class LaunchActivity: Activity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndAutoLogin();
    }

    fun checkAndAutoLogin(){

    }
}