package com.agera.hometools.bean

import android.util.Log

/**
 * Created by Agera on 2018/4/27.
 */
class Friends(var results: List<Map<String, String>>) {

    fun getFriends(): List<String> {
        var friends = emptyList<String>()
        try {
            if (results == null || results.size == 0)
                return friends
            var friendStr = results[0]["friends"]?.replace("\\", "")
            Log.e("---","---getFriends-001--: $friendStr")
            friendStr?.split(",")?.forEach {
                Log.e("---","---getFriends-item--: $it")
                friends.plus(it)
            }

        } catch (e: Exception) {
            Log.e("---", "--getFriends appear error: ${e.message}")
        }

        return friends
    }

}