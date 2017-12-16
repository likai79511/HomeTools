package com.agera.hometools.locate

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.agera.hometools.bean.LocationData
import com.agera.hometools.push.PushImp
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by mac on 2017/12/15.
 */
class LocationService : Service() {

    var to: String? = null
    private var mTimeFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd  HH:mm:ss")
    private var locationClient: AMapLocationClient? = null

    override fun onBind(intent: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        to = intent?.getStringExtra("to")
        getLocation()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = AMapLocationClient(this)
    }

    fun getLocation(): LocationData? {

        var options = AMapLocationClientOption()
        locationClient?.setLocationListener {
            if (it == null) {
                Log.e("---", "---location is null")
            } else {
                var data = LocationData(it.latitude, it.longitude, it.accuracy, mTimeFormat.format(Date(it.time)), it.address)
                to?.let {
                    PushImp.instance().sendLocationTo(data,it)
                }
            }
            stopSelf()
        }
        options.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        locationClient?.setLocationOption(options)
        locationClient?.startLocation()

        return null
    }


    override fun onDestroy() {
        super.onDestroy()
        locationClient?.stopLocation()
    }

}