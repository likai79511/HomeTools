package com.agera.hometools.core

import android.os.Handler
import android.os.Looper
import java.util.concurrent.*

/**
 * Created by Agera on 2017/11/8.
 */
class TaskDriver private constructor() {
    val cpuCount: Int = Runtime.getRuntime().availableProcessors()

    val mCore: ExecutorService = Executors.newFixedThreadPool(cpuCount + 1)

    val mControl: Semaphore = Semaphore(cpuCount)

    val mainHandler: Handler = Handler(Looper.getMainLooper())

    val mainExecutor: Executor = Executor {
        mainHandler.post(it)
    }

    companion object {
        private val instance: TaskDriver = TaskDriver()
        fun instance(): TaskDriver = instance
    }

    fun execute(task: HttpTask) {
        mCore.submit(task)
    }

    fun getThreadPool(): ExecutorService {
        return mCore
    }

    fun getMainThreadExecutor(): Executor {
        return mainExecutor
    }

}