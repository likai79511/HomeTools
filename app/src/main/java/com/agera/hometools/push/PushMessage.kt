package com.agera.hometools.push

/**
 * Created by mac on 2017/12/14.
 */
class PushMessage{

    var platform:String ? = null

    var audience:HashMap<String,ArrayList<String>> ?= null

    var message: JPUSHMessage?=null

    class JPUSHMessage constructor(var msg:String){
        var msg_content:String ?=null
        init {
            msg_content = msg
        }
    }


}