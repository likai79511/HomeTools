package com.agera.hometools.locate.friends

import android.text.TextUtils
import android.util.Log
import com.agera.hometools.bean.Friends
import com.agera.hometools.core.TaskDriver
import com.agera.hometools.network.Restful
import com.google.gson.Gson

/**
 * Created by Agera on 2018/4/26.
 */
class FriendsImp {

    companion object {
        private var instance = FriendsImp()
        fun instance() = instance
    }


    fun getFriends(): List<String>? {
        var friends: List<String> = emptyList()
        var startTime = System.currentTimeMillis()
        TaskDriver.instance().execute(Restful.instance().queryUserInfo())
                .ifSucceededSendTo {

                    var response = it.bodyString.get()?.trim()
                    Log.e("---", "--request time: " + (System.currentTimeMillis() - startTime)+"\nresponse:$response")
                    TextUtils.isEmpty(response) ?: return@ifSucceededSendTo
                    friends = Gson().fromJson(response, Friends::class.java).getFriends()
                    friends.forEach {
                        Log.e("---", "---parse friends: $it")
                    }

                }
                .ifFailedSendTo {
                    Log.e("---", "---error:" + it.message)
                }
        return friends
    }
}