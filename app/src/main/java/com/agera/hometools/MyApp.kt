package com.agera.hometools

import android.app.Application
import cn.jpush.android.api.JPushInterface

/**
 * Created by Agera on 2017/10/24.
 */
class MyApp: Application() {


    companion object {
        private var app: MyApp? = null
        fun instance() = app!!
    }

    var userName = "12345678901"

    override fun onCreate() {
        super.onCreate()
        app = this

        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
    }

}