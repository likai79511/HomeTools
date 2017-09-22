package com.agera.hometools.network;


import com.google.android.agera.net.HttpResponse;

/**
 * Created by 43992639 on 2017/9/22.
 */
public abstract class Callback {
    public abstract void call(HttpResponse result);

    public void error(Throwable e) {

    }

}
