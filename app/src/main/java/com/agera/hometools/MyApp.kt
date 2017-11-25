package com.agera.hometools

import android.app.Application

/**
 * Created by mac on 2017/10/24.
 */
class MyApp: Application() {
    companion object {
        private var app: MyApp? = null
        fun instance(): MyApp {
            return app!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }
}