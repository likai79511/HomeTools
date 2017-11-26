package com.agera.hometools.push

import android.content.Context
import android.content.Intent
import android.util.Log
import cn.jpush.android.api.JPushInterface
import cn.jpush.android.service.PushReceiver

/**
 * Created by Administrator on 2017/11/26 0026.
 */
class PushReceiver : PushReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {
        super.onReceive(p0, p1)
        Log.e("---", "--get onReceive")
        var detail: String = ""
        p1?.let {
            var b = p1.extras
            b.keySet().forEach {
                detail += "$it = ${b.get(it)}\n"
            }
            Log.e("---", "--onReceive data: $detail")
            var id = JPushInterface.getRegistrationID(p0)
            Log.e("---", "--id:$id")
        }
    }
}