package com.agera.hometools.core

import com.agera.hometools.network.Callback
import com.google.android.agera.Result
import com.google.android.agera.net.HttpResponse
import java.util.concurrent.Callable
import java.util.concurrent.FutureTask

/**
 * Created by Administrator on 2017/11/2 0002.
 */
class HttpTask(cb: Callable<Result<HttpResponse>>) : FutureTask<Result<HttpResponse>>(cb) {

    private var isLocked: Boolean = false

    private var error: Throwable? = null

    private var callable: Callback? = null

    constructor(cb:Callable<Result<HttpResponse>>,)

}