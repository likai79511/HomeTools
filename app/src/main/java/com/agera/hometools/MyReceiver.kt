package com.agera.hometools

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import cn.jpush.android.api.JPushInterface

/**
 * Created by mac on 2017/12/13.
 */
class MyReceiver:BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.e("---","----onReceive:${intent?.action}")

        var action = intent?.action

        var data = intent?.extras

        if (action==null)
            return

        when(action){
            JPushInterface.ACTION_REGISTRATION_ID -> {
                Log.e("---","receive register id:${data?.get(JPushInterface.EXTRA_REGISTRATION_ID)}")
            }
            JPushInterface.ACTION_MESSAGE_RECEIVED -> {
//                MyApp.instance().activity?.addMsg(data?.get(JPushInterface.EXTRA_MESSAGE) as String)
                Log.e("---","receive custome message:${data?.get(JPushInterface.EXTRA_MESSAGE)}")
            }
            JPushInterface.ACTION_NOTIFICATION_RECEIVED ->{}
            JPushInterface.ACTION_NOTIFICATION_OPENED ->{}
            else->{
//                Log.e("---","can not resolve this message")
            }

        }

    }
}