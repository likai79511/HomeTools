package com.agera.hometools.locate

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.agera.hometools.BaseActivity
import com.agera.hometools.R
import com.agera.hometools.locate.friends.FriendsActivity
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MyLocationStyle

/**
 * Created by Agera on 2017/11/8.
 */
class LocateActivity : BaseActivity() {
    private var mMap: MapView? = null
    private var mMapControl: AMap? = null

    private var locationStyle: MyLocationStyle = MyLocationStyle()
    private var isShowInCenter = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.locate_view)

        initView(savedInstanceState)
        locationStyle.interval(2500)
        locationStyle.showMyLocation(true)
        locationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER)

        initClickEvents()

    }

    fun initClickEvents() {
        findViewById(R.id.btn_friends).setOnClickListener {
            startActivity(Intent(this, FriendsActivity::class.java))
        }
    }


    private fun initView(savedInstanceState: Bundle?) {
        mMap = findViewById(R.id.map) as MapView
        mMap!!.onCreate(savedInstanceState)
        if (mMap != null)
            mMapControl = mMap!!.map
        initMap(true)
        mMapControl?.let {
            it.moveCamera(CameraUpdateFactory.zoomTo(14f))
//            it.isTrafficEnabled = true
            it.uiSettings?.isZoomControlsEnabled = false
            it.uiSettings?.isMyLocationButtonEnabled = true
            it.setOnMyLocationChangeListener {
                if (isShowInCenter) {
                    mMapControl?.moveCamera(CameraUpdateFactory.changeLatLng(LatLng(it.latitude, it.longitude)))
                    isShowInCenter = false
                }
            }
        }

    }

    private fun initMap(flag: Boolean) {
        mMapControl!!.myLocationStyle = locationStyle
        mMapControl!!.isMyLocationEnabled = flag
        mMapControl!!.uiSettings?.isMyLocationButtonEnabled = true
        isShowInCenter = true
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
        Log.e("---", "---map onPause")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        mMap!!.onSaveInstanceState(outState)
    }

}