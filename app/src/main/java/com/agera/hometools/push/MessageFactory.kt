package com.agera.hometools.push

import com.agera.hometools.utils.AppendList
import com.agera.hometools.utils.AppendMap
import com.agera.hometools.utils.CommonUtils
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


    fun createMessage(customMsg:CustomMessage):PushMessage{
        var msg:PushMessage = PushMessage()
        msg.platform = "all"
        msg.audience = AppendMap<ArrayList<String>>().put("alias",AppendList<String>().add(customMsg.to).compile()).compile()
        msg.message = PushMessage.JPUSHMessage(CommonUtils.instance().gson.toJson(customMsg))
        return msg
    }
}