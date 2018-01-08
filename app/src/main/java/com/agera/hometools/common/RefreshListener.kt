package com.agera.hometools.common

import android.support.v4.widget.SwipeRefreshLayout
import com.google.android.agera.BaseObservable

/**
 * Created by Agera on 2018/1/8.
 */
class RefreshListener: SwipeRefreshLayout.OnRefreshListener, BaseObservable(){
    override fun onRefresh() {
        dispatchUpdate()
    }
}