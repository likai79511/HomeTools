package com.agera.hometools.core;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;

/**
 * Created by Administrator on 2017/9/20.
 */

public class TaskDriver {

    private static final ExecutorService mCore = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() + 1);

    public static Semaphore maxCurrentCount = new Semaphore(8);

    private TaskDriver() {
    }

    private static TaskDriver driver = null;


    public static TaskDriver instance() {
        synchronized (TaskDriver.class) {
            if (driver == null)
                driver = new TaskDriver();
            return driver;
        }
    }

    public <T> T execute(FutureTask<T> task) {
        mCore.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        });
    }

}
