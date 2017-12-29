package com.agera.hometools.push

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import cn.jpush.android.api.JPushInterface
import com.agera.hometools.MyApp
import com.agera.hometools.bean.LocationData
import com.agera.hometools.locate.LocationService
import com.agera.hometools.utils.CommonUtils
import com.agera.hometools.utils.Constants

/**
 * Created by mac on 2017/12/13.
 */
class MessageReceiver : BroadcastReceiver() {

    var mContext: Context? = null
    override fun onReceive(context: Context?, intent: Intent?) {
        mContext = context
        Log.e("---", "----onReceive:${intent?.action}")

        var action: String? = intent?.action ?: return

        var data = intent?.extras ?: return


        when (action) {
            JPushInterface.ACTION_MESSAGE_RECEIVED -> {
                handleMessage(data.get(JPushInterface.EXTRA_MESSAGE)?.toString())
            }
        }

    }

    private fun handleMessage(message: String?) {
        message?:return
        Log.e("---", "receive custome message:$message")
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