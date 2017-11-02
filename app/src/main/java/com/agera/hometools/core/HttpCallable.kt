package com.agera.hometools.core

import com.google.android.agera.Result
import com.google.android.agera.net.HttpFunctions
import com.google.android.agera.net.HttpRequest
import com.google.android.agera.net.HttpResponse
import java.util.concurrent.Callable

/**
 * Created by Administrator on 2017/11/2 0002.
 */
class HttpCallable(val req: HttpRequest) : Callable<Result<HttpResponse>> {
    var request: HttpRequest? = null
    init {
        request = req
    }

    override fun call(): Result<HttpResponse>? {
        if (request == null)
            return Result.failure(Throwable("request is null"))
        return request?.let { HttpFunctions.httpFunction().apply(it) }
    }
}