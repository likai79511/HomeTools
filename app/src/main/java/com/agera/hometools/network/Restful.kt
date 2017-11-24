package com.agera.hometools.network

import com.agera.hometools.core.HttpCallable
import com.agera.hometools.core.HttpTask
import com.agera.hometools.utils.AppendMap
import com.google.android.agera.net.HttpRequests
import com.google.gson.Gson

/**
 * Created by Agera on 2017/11/8.
 */
class Restful {
    //regist
    companion object {
        private val user_url = "https://api.bmob.cn/1/classes/user"
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

        fun register(name: String, password: String): HttpTask {
            return HttpTask(HttpCallable(HttpRequests.httpPostRequest(user_url)
                    .body(gson.toJson(AppendMap().put(TELEPHONE, name)
                            .put(PASSWORD, password)
                            .compile()).toByteArray())
                    .headerField(applicationIdDesc, applicationId)
                    .headerField(rest_keyDesc, rest_key)
                    .headerField(content_type, format_jason)
                    .connectTimeoutMs(timeout)
                    .readTimeoutMs(timeout)
                    .compile()))
        }

        //login
        fun login(name: String, password: String): HttpTask {
            return HttpTask(HttpCallable(HttpRequests.httpGetRequest("$user_url?where={\"telephone\":\"$name\",\"password\":\"$password\"}")
                    .headerField(applicationIdDesc, applicationId)
                    .headerField(rest_keyDesc, rest_key)
                    .headerField(content_type, format_jason)
                    .connectTimeoutMs(timeout)
                    .readTimeoutMs(timeout)
                    .compile()))
        }
    }
}