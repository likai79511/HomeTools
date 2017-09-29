package com.agera.hometools.network;


import com.google.android.agera.net.HttpResponse;

/**
 * Created by  Agera K on 2017/9/22.
 */
public abstract class Callback {
    public abstract void success(HttpResponse result);

    public void error(Throwable e) {

    }

}
