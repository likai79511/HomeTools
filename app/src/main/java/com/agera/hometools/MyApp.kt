package com.agera.hometools

import android.app.Application
import com.avos.avoscloud.AVOSCloud

/**
 * Created by mac on 2017/10/24.
 */
class MyApp: Application() {

    //LeanCloud
    var appIp = "35JlrWrdcCNCd73E12rYtFe5-gzGzoHsz"
    var appKey = "p4cUl98LKAsLd2jdgPzE2CkA"
    companion object {
        private var app: MyApp? = null
        fun instance(): MyApp {
            return app!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        app = this

        //init leanCloud
        AVOSCloud.initialize(this,appIp,appKey)
        AVOSCloud.setDebugLogEnabled(true)
    }
}