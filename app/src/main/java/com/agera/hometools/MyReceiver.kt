package com.agera.hometools

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import cn.jpush.android.api.JPushInterface
import com.agera.hometools.bean.LocationData
import com.agera.hometools.locate.LocationService
import com.agera.hometools.push.PushMessage
import com.agera.hometools.utils.CommonUtils
import com.agera.hometools.utils.Constants

/**
 * Created by mac on 2017/12/13.
 */
class MyReceiver : BroadcastReceiver() {

    var mContext: Context? = null
    override fun onReceive(context: Context?, intent: Intent?) {
        mContext = context
        Log.e("---", "----onReceive:${intent?.action}")

        var action = intent?.action

        var data = intent?.extras

        if (action == null)
            return

        when (action) {
            JPushInterface.ACTION_REGISTRATION_ID -> {
                Log.e("---", "receive register id:${data?.get(JPushInterface.EXTRA_REGISTRATION_ID)}")
            }
            JPushInterface.ACTION_MESSAGE_RECEIVED -> {
                handleMessage(data?.get(JPushInterface.EXTRA_MESSAGE)?.toString())
            }
            JPushInterface.ACTION_NOTIFICATION_RECEIVED -> {
            }
            JPushInterface.ACTION_NOTIFICATION_OPENED -> {
            }
            else -> {
//                Log.e("---","can not resolve this message")
            }

        }

    }

    private fun handleMessage(message: String?) {

        Log.e("---", "receive custome message:$message")
        if (TextUtils.isEmpty(message))
            return
        MyApp.instance().activity?.addMsg(message!!)
        try {
            var cm = CommonUtils.instance().gson.fromJson(message, PushMessage.CustomMessage::class.java)
            when (cm.type) {
                Constants.MESSAGE_REQUIRE_LOCATION -> {
                    var service: Intent = Intent(mContext, LocationService::class.java)
                    service.putExtra("to", cm.from)
                    mContext?.startService(service)
                }
                Constants.MESSAGE_TRANSFER_LOCATION -> {
                    var from = cm.from
                    var location = CommonUtils.instance().gson.fromJson<LocationData>(cm.data, LocationData::class.java)
                    Log.e("---", "--receive $from -location: $location")
                }
            }
        } catch (e: Exception) {
            Log.e("---", "--message deserized failed--:${e.message}")
        }
    }
}