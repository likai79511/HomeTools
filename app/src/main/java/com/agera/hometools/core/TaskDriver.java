package com.agera.hometools.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;

/**
 * Created by Administrator on 2017/9/20.
 */

public class TaskDriver {

    private final int cpuCount = Runtime.getRuntime().availableProcessors();

    private final ExecutorService mCore = Executors.newFixedThreadPool(cpuCount + 1);

    public final Semaphore mControl = new Semaphore(8);

    private static TaskDriver driver = null;

    private TaskDriver() {
    }


    public static TaskDriver instance() {
        synchronized (TaskDriver.class) {
            if (driver == null)
                driver = new TaskDriver();
        }
        return driver;
    }


    public void execute(FutureTask task) {
        mCore.submit(task);
    }

    public ExecutorService getThreadPool() {
        return mCore;
    }


}
