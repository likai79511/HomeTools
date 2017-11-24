package com.agera.hometools

import android.app.Application

/**
 * Created by mac on 2017/10/24.
 */
class MyApp private constructor() : Application() {

    companion object {
        private var app: MyApp  = null

        public fun getInstance(): MyApp {
            synchronized(this) {
                if (app == null)
                    app = MyApp()
            }
            return app
        }
    }
}