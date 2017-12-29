package com.agera.hometools.core

import android.os.Handler
import android.os.Looper
import com.google.android.agera.Result
import com.google.android.agera.net.HttpFunctions
import com.google.android.agera.net.HttpRequest
import com.google.android.agera.net.HttpResponse
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Semaphore

/**
 * Created by Agera on 2017/11/8.
 */
class TaskDriver private constructor() {
    val cpuCount: Int = Runtime.getRuntime().availableProcessors()

    val mCore: ExecutorService = Executors.newCachedThreadPool()

    private val mControl: Semaphore = Semaphore(if (cpuCount > 4) cpuCount else 4)

    val mainHandler: Handler = Handler(Looper.getMainLooper())

    private val mainExecutor: Executor = Executor {
        mainHandler.post(it)
    }

    companion object {
        private val instance: TaskDriver = TaskDriver()
        fun instance(): TaskDriver = instance
    }

    fun execute(task: HttpRequest): Result<HttpResponse> {
        return try {
            mControl.acquire()
            HttpFunctions.httpFunction().apply(task)
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            mControl.release()
        }
    }

    fun getThreadPool(): ExecutorService {
        return mCore
    }

    fun getMainThreadExecutor(): Executor {
        return mainExecutor
    }

}