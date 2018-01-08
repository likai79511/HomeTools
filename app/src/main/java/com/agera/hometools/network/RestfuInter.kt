package com.agera.hometools.network

import com.agera.hometools.push.PushMessage
import com.google.android.agera.net.HttpRequest

/**
 * Created by Agera on 2017/12/14.
 */
interface RestfuInter {
    fun register(name: String, password: String): HttpRequest

    fun login(name: String, password: String): HttpRequest

    fun sendMessage(msg: PushMessage): HttpRequest
}