package com.agera.hometools.network;

import com.agera.hometools.core.HttpCallable;
import com.agera.hometools.core.HttpTask;
import com.google.android.agera.net.HttpRequests;
import com.google.gson.Gson;


/**
 * Created by  Agera K on 2017/9/22.
 */
public class Restful {

    private static final String user_url = "https://api.bmob.cn/1/classes/user";
    private static int timeout = 10_1000;

    private static String applicationIdDesc = "X-Bmob-Application-Id";
    private static String rest_keyDesc = "X-Bmob-REST-API-Key";

    private static String applicationId = "fc50317a2f1dc14576f7c600697e6799";
    private static String rest_key = "51c117839a06c75aa380eb78eb8395b7";
    private static String content_type = "Content-Type";
    private static String format_jason = "application/json";

    private static Gson gson = new Gson();

    private static final String TELEPHONE = "telephone";
    private static final String PASSWORD = "password";

    //regist
    public static HttpTask register(String name, String password,Callback cb) {
        return HttpTask.createHttpTask(new HttpCallable(HttpRequests.httpPostRequest(user_url)
                .body(gson.toJson(new AppendMap().put(TELEPHONE, name).put(PASSWORD, password).compile()).getBytes())
                .headerField(applicationIdDesc, applicationId)
                .headerField(rest_keyDesc, rest_key)
                .headerField(content_type, format_jason)
                .connectTimeoutMs(timeout)
                .readTimeoutMs(timeout)
                .compile()),cb);
    }


    //login
    public static HttpTask login(String name,String password,Callback cb){
        return HttpTask.createHttpTask(new HttpCallable(HttpRequests.httpGetRequest(user_url+"?where={\"telephone\":\""+name+"\",\"password\":\""+password+"\"}")
        .headerField(applicationIdDesc,applicationId)
        .headerField(rest_keyDesc,rest_key)
        .headerField(content_type,format_jason)
        .connectTimeoutMs(timeout)
        .readTimeoutMs(timeout)
        .compile()),cb);
    }
}
