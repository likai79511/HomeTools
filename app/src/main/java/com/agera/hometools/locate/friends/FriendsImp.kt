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


    fun getFriends(): ArrayList<String>? {
        var friends = ArrayList<String>()
        var startTime = System.currentTimeMillis()
        TaskDriver.instance().execute(Restful.instance().queryUserInfo())
                .ifSucceededSendTo {
                    var response = it.bodyString.get()?.trim()
                    TextUtils.isEmpty(response) ?: return@ifSucceededSendTo
                    friends = Gson().fromJson(response, Friends::class.java).getFriends()
                }
                .ifFailedSendTo {
                    Log.e("---", "---error:" + it.message)
                }
        return friends
    }
}