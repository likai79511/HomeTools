package com.agera.hometools.network

import com.google.android.agera.net.HttpResponse

/**
 * Created by Agera on 2017/11/8.
 */
abstract class Callback {

    abstract fun success(resp: HttpResponse)

    fun error(e: Throwable) {
    }
}