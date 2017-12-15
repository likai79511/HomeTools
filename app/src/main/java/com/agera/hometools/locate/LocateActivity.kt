package com.agera.hometools.locate

import android.app.Activity
import android.location.Location
import android.os.Bundle
import android.util.Log
import com.agera.hometools.R
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.MyLocationStyle

/**
 * Created by Agera on 2017/11/8.
 */
class LocateActivity : Activity() {
    var mMap: MapView? = null
    var mMapControl: AMap? = null

    var locationStyle: MyLocationStyle = MyLocationStyle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.locate_view)

        initView(savedInstanceState)
        locationStyle.interval(2000)
        locationStyle.showMyLocation(true)
    }

    fun initView(savedInstanceState: Bundle?) {
        mMap = findViewById(R.id.map) as MapView?
        mMap!!.onCreate(savedInstanceState)
        if (mMap != null)
            mMapControl = mMap!!.map
        initMap(true)
        mMapControl?.moveCamera(CameraUpdateFactory.zoomTo(14f))

    }

    fun initMap(flag:Boolean) {
        mMapControl!!.myLocationStyle = locationStyle
        mMapControl!!.isMyLocationEnabled = flag
        mMapControl!!.setOnMyLocationChangeListener(LocationChange)
    }


    var LocationChange = object:AMap.OnMyLocationChangeListener{
        override fun onMyLocationChange(p0: Location?) {
            if (p0 != null) {
                Log.e("---", "---location: ${p0.latitude},${p0.longitude}")
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
        initMap(true)
    }

    override fun onPause() {
        super.onPause()
        mMap!!.onPause()
        initMap(false)
        Log.e("---","---map onPause")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mMap!!.onSaveInstanceState(outState)
    }

}