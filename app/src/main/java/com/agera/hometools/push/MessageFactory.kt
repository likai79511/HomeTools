package com.agera.hometools.push

import com.agera.hometools.utils.AppendList
import com.agera.hometools.utils.AppendMap
import com.google.gson.Gson

/**
 * Created by mac on 2017/12/14.
 */
class MessageFactory private constructor(){

    companion object{
        private  var factory = MessageFactory()

        fun instance():MessageFactory = factory

        private var gson:Gson = Gson()

    }


    fun createMessage(msg:CustomMessage):String{
        var message:PushMessage = PushMessage()
        message.platform = "all"
        message.audience = AppendMap<ArrayList<String>>().put("alias",AppendList<String>().add(msg.to).compile()).compile()
        message.message = PushMessage.JPUSHMessage(gson.toJson(msg))
        return gson.toJson(message)
    }
}