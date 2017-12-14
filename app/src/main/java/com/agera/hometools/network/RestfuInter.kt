package com.agera.hometools.network

import com.agera.hometools.core.HttpTask
import com.agera.hometools.push.PushMessage

/**
 * Created by mac on 2017/12/14.
 */
interface RestfuInter {
    fun register(name: String, password: String): HttpTask

    fun login(name: String, password: String): HttpTask

    fun sendMessage(msg:PushMessage.JPUSHMessage)
}