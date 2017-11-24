package com.agera.hometools.core

import com.google.android.agera.Result
import com.google.android.agera.net.HttpResponse
import java.util.concurrent.Callable
import java.util.concurrent.FutureTask

/**
 * Created by Agera on 2017/11/2 0002.
 */
class HttpTask(cb: Callable<Result<HttpResponse>>) : FutureTask<Result<HttpResponse>>(cb) {


    private var error: Throwable? = null

    override fun run() {
        try {
            TaskDriver.instance().mControl.acquire()
            if (!Thread.interrupted())
                super.run()
        } catch (e: Exception) {
            error = e
        }
    }

    override fun done() {
        super.done()
        TaskDriver.instance().mControl.release()
    }

    override fun get(): Result<HttpResponse> {
        if (error != null) {
            return Result.failure(error!!)
        }
        return super.get()
    }
}