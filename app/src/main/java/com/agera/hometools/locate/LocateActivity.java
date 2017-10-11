package com.agera.hometools.locate;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.agera.hometools.R;
import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;

/**
 * Created by Agera K on 2017/10/10.
 */
public class LocateActivity extends Activity {
    private MapView mMap = null;
    private AMap mMapControl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locate_view);
        initView(savedInstanceState);
    }

    private void initView(Bundle savedInstanceState) {
        mMap = (MapView) findViewById(R.id.map);
        mMap.onCreate(savedInstanceState);
        if (mMap != null)
            mMapControl = mMap.getMap();
        initMap();
    }

    private void initMap() {
        MyLocationStyle locationStyle = new MyLocationStyle();
        locationStyle.interval(2000);
        locationStyle.showMyLocation(true);
        mMapControl.setMyLocationStyle(locationStyle);
        mMapControl.setMyLocationEnabled(true);
        mMapControl.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (location != null) {
                    Log.e("---", "---locaton:" + location.getLatitude() + "," + location.getLongitude());
                }else {
                    Log.e("---","---location is null");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMap.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMap.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMap.onSaveInstanceState(outState);
    }
}
