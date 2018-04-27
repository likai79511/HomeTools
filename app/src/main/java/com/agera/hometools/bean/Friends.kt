package com.agera.hometools.bean

import android.text.TextUtils
import android.util.Log

/**
 * Created by Agera on 2018/4/27.
 */
class Friends(var results: List<Map<String, String>>) {

    fun getFriends(): ArrayList<String> {
        var friends = ArrayList<String>()
        try {
            if (results == null || results.size == 0)
                return friends
            var friendStr = results[0]["friends"]?.replace("\\", "")?.replace("\"", "")
            friendStr?.split(",")?.forEach {
                if (!TextUtils.isEmpty(it)){
                    friends.add(it)
                }
            }

        } catch (e: Exception) {
            Log.e("---", "--getFriends appear error: ${e.message}")
        }
        return friends
    }

}