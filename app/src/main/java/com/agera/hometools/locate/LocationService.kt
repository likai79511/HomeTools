package com.agera.hometools.locate

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import com.agera.hometools.bean.LocationData
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by mac on 2017/12/15.
 */
class LocationService(val ctx: Context,var to:String,name: String = "location") : IntentService(name) {

    var mTimeFormat: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd  HH:mm:ss")
    var locationClient = AMapLocationClient(ctx)
    override fun onHandleIntent(intent: Intent?) {

        var data = getLocation()

    }

    fun getLocation(): LocationData? {

        var options = AMapLocationClientOption()
        options.isOnceLocation = true
        locationClient.setLocationListener {
            if (it == null) {
                Log.e("---", "---location is null")
            } else {
                var lat = it.latitude
                var long = it.longitude
                var accur = it.accuracy
                var time = mTimeFormat.format(Date(it.time))
                Log.e("---", "---location is $it")
            }
        }
        options.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
        locationClient.setLocationOption(options)

        return null
    }


    override fun onDestroy() {
        super.onDestroy()
        locationClient?.stopLocation()
        Log.e("---","---onDestroy---")
    }
}