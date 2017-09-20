package com.agera.hometools.core;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Created by Administrator on 2017/9/20.
 */

public class Task extends FutureTask {

    private Callable Callable = null;
    private Runnable Runnable = null;

    public Task(Callable callable) {
        super(callable);
        Callable = callable;
    }

    public Task(Runnable runnable, Object result) {
        super(runnable, result);
        Runnable = runnable;
    }


}
