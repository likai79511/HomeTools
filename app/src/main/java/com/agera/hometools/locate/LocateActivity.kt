package com.agera.hometools.locate

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.agera.hometools.R
import com.amap.api.maps.AMap
import com.amap.api.maps.MapView
import com.amap.api.maps.model.MyLocationStyle

/**
 * Created by Agera on 2017/11/8.
 */
class LocateActivity : Activity() {
    var mMap: MapView? = null
    var mMapControl: AMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.locate_view)
        initView(savedInstanceState)
    }

    fun initView(savedInstanceState: Bundle?) {
        mMap = findViewById(R.id.map) as MapView?
        mMap!!.onCreate(savedInstanceState)
        if (mMap != null)
            mMapControl = mMap!!.map
        initMap()
    }

    fun initMap() {
        var locationStyle: MyLocationStyle = MyLocationStyle()
        locationStyle.interval(2000)
        locationStyle.showMyLocation(true)
        mMapControl!!.myLocationStyle = locationStyle
        mMapControl!!.isMyLocationEnabled = true
        mMapControl!!.setOnMyLocationChangeListener {
            if (it != null) {
                Log.e("---", "---location: ${it.latitude},${it.longitude}")
            } else {
                Log.e("---", "---location is null")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mMap!!.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        mMap!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMap!!.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mMap!!.onSaveInstanceState(outState)
    }
}