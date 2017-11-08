package com.agera.hometools.core

import com.agera.hometools.network.Callback
import com.google.android.agera.Receiver
import com.google.android.agera.Result
import com.google.android.agera.net.HttpResponse
import java.util.concurrent.Callable
import java.util.concurrent.FutureTask
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by Agera on 2017/11/2 0002.
 */
class HttpTask(cb: Callable<Result<HttpResponse>>) : FutureTask<Result<HttpResponse>>(cb) {

    private var isLocked: AtomicBoolean = AtomicBoolean(false)

    private var error: Throwable? = null

    private var callable: Callback? = null

    private constructor(cb: Callable<Result<HttpResponse>>, cb2: Callback) : this(cb) {
        callable = cb2
    }

    companion object {
        fun createHttpTask(cb: Callable<Result<HttpResponse>>, cb2: Callback): HttpTask {
            return HttpTask(cb, cb2)
        }
    }

    override fun run() {
        try {
            TaskDriver.instance().mControl.acquire()
            isLocked.compareAndSet(false, true)
            if (!Thread.interrupted())
                super.run()
        } catch (e: Exception) {
            isLocked.compareAndSet(true, false)
            error = e
        }
    }

    override fun done() {
        if (callable != null) {
            try {
                get()
                        .ifFailedSendTo(Receiver { callable!!.error(it) })
                        .ifSucceededSendTo(Receiver {
                            if (it.responseCode <= 300 && it.responseCode >= 200) {
                                callable!!.success(it)
                            } else {
                                callable!!.error(Exception("the request is failed, responseCode:${it.responseCode}"))
                            }
                        })
            } catch (e: Exception) {
                callable!!.error(e)
            }
        }
        if (isLocked.get()) {
            TaskDriver.instance().mControl.release()
            isLocked.compareAndSet(true, false)
        }
    }

    override fun get(): Result<HttpResponse> {
        if (error != null) {
            return Result.failure(error!!)
        }
        return super.get()
    }
}