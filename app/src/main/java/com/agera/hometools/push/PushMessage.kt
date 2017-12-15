package com.agera.hometools.push

/**
 * Created by mac on 2017/12/14.
 */
class PushMessage{

    var platform:String ? = null

    var audience:HashMap<String,ArrayList<String>> ?= null

    var message: JPUSHMessage?=null

    data class JPUSHMessage(var msg_content:String)


    data class CustomMessage(val type:Int,val to:String,val from:String)
}