package com.agera.hometools.network

import com.agera.hometools.push.PushMessage
import com.agera.hometools.utils.AppendMap
import com.agera.hometools.utils.CommonUtils
import com.agera.hometools.utils.Constants
import com.google.android.agera.net.HttpRequest
import com.google.android.agera.net.HttpRequests
import com.google.gson.Gson

/**
 * Created by Agera on 2017/11/8.
 */
class Restful private constructor() : RestfuInter {
    //regist
    companion object {
        private val user_url = "https://api.bmob.cn/1/classes/user"
        private val push_url = "https://bjapi.push.jiguang.cn/v3/push"


        private val timeout = 101000

        private val applicationIdDesc = "X-Bmob-Application-Id"
        private val rest_keyDesc = "X-Bmob-REST-API-Key"

        private val applicationId = "fc50317a2f1dc14576f7c600697e6799"
        private val rest_key = "51c117839a06c75aa380eb78eb8395b7"
        private val content_type = "Content-Type"
        private val format_jason = "application/json"

        private val gson = Gson()

        private val TELEPHONE = "telephone"
        private val PASSWORD = "password"
        private val FRIENDS = "friends"
        private val AUTHORIZATION = "Authorization"

        private var restful = Restful()

        fun instance(): Restful = restful


    }

    override fun register(name: String, password: String): HttpRequest {
        return HttpRequests.httpPostRequest(user_url)
                .body(gson.toJson(AppendMap<String>().put(TELEPHONE, name)
                        .put(PASSWORD, password)
                        .put(FRIENDS, "")
                        .compile()).toByteArray())
                .headerField(applicationIdDesc, applicationId)
                .headerField(rest_keyDesc, rest_key)
                .headerField(content_type, format_jason)
                .connectTimeoutMs(timeout)
                .readTimeoutMs(timeout)
                .compile()
    }

    //login
    override fun login(name: String, password: String): HttpRequest {
        return HttpRequests.httpGetRequest("$user_url?where={\"telephone\":\"$name\",\"password\":\"$password\"}")
                .headerField(applicationIdDesc, applicationId)
                .headerField(rest_keyDesc, rest_key)
                .headerField(content_type, format_jason)
                .connectTimeoutMs(timeout)
                .readTimeoutMs(timeout)
                .compile()
    }

    //send push message
    override fun sendMessage(msg: PushMessage): HttpRequest {
        return HttpRequests.httpPostRequest(push_url)
                .body(CommonUtils.instance().gson.toJson(msg).toByteArray())
                .headerField(content_type, format_jason)
                .headerField(AUTHORIZATION, Constants.AUTHORIZATION)
                .connectTimeoutMs(timeout)
                .readTimeoutMs(timeout)
                .compile()
    }
}