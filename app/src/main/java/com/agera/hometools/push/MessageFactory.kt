package com.agera.hometools.push

import com.agera.hometools.utils.AppendList
import com.agera.hometools.utils.AppendMap
import com.google.gson.Gson

/**
 * Created by Agera on 2017/12/14.
 */
class MessageFactory private constructor(){

    companion object{
        private  var factory = MessageFactory()

        fun instance():MessageFactory = factory

        private var gson:Gson = Gson()

    }

    fun createPushMessage(messageType:Int,to:String,from:String,data:String):PushMessage{
        var msg = PushMessage()
        msg.platform = "all"
        msg.audience = AppendMap<ArrayList<String>>().put("alias",AppendList<String>().add(to).compile()).compile()
        msg.message = PushMessage.JPUSHMessage(gson.toJson(PushMessage.CustomMessage(messageType,to,from,data)))
        return msg
    }
}