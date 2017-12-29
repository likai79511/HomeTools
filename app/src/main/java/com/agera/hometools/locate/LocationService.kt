package com.agera.hometools.locate

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.text.TextUtils
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
    var maxTry = 3
    var tryCount = 0

    override fun onBind(intent: Intent?): IBinder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        tryCount = 0
        locationClient = AMapLocationClient(this)
        to = intent?.getStringExtra("to")?:return super.onStartCommand(intent, flags, startId)
        getLocation()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun getLocation(): LocationData? {

        var options = AMapLocationClientOption()
        locationClient?.setLocationListener {
            Log.e("---", "---location address: ${it.address}")
            if ((it==null || TextUtils.isEmpty(it.address)) && tryCount<=maxTry){
                tryCount++
                locationClient?.startLocation()
            }else {
                var data = LocationData(it.latitude, it.longitude, it.accuracy, mTimeFormat.format(Date(it.time)), it.address)
                Log.e("---","--location:$data")
                PushImp.instance().sendLocationTo(data,to!!)
            }
            stopSelf()
        }
        options.isOnceLocation = true
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