package com.agera.hometools.core;

import android.support.annotation.NonNull;
import android.util.Log;

import com.agera.hometools.network.Callback;
import com.google.android.agera.Receiver;
import com.google.android.agera.Result;
import com.google.android.agera.net.HttpResponse;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by Administrator on 2017/9/20.
 */

public class HttpTask extends FutureTask<Result<HttpResponse>> {

    private boolean isLocked = false;

    private Throwable error = null;

    private Callback callable = null;

    private HttpTask(@NonNull Callable<Result<HttpResponse>> callable) {
        super(callable);
    }

    private HttpTask(@NonNull Callable<Result<HttpResponse>> callable, Callback cb) {
        super(callable);
        this.callable = cb;
    }

    public static HttpTask createHttpTask(@NonNull Callable<Result<HttpResponse>> callable,Callback callback) {
        return new HttpTask(callable,callback);
    }

    @Override
    protected void done() {
        if (callable != null) {
            try {
                get()
                        .ifFailedSendTo(new Receiver<Throwable>() {
                            @Override
                            public void accept(@NonNull Throwable value) {
                                callable.error(value);
                            }
                        })
                        .ifSucceededSendTo(new Receiver<HttpResponse>() {
                            @Override
                            public void accept(@NonNull HttpResponse value) {
                                if (value.getResponseCode() <= 300 && value.getResponseCode() >= 200) {
                                    callable.success(value);
                                } else {
                                    callable.error(new Exception("the request is failed, responseCode:" + value.getResponseCode()));
                                }
                            }
                        });
            } catch (Exception e) {
                callable.error(e);
            }
        }

        if (isLocked) {
            TaskDriver.instance().mControl.release();
            isLocked = false;
        }
    }

    @Override
    public void run() {
        try {
            TaskDriver.instance().mControl.acquire();
            isLocked = true;
            if (!Thread.interrupted()) {
                super.run();
            }
        } catch (Exception e) {
            isLocked = false;
            error = e;
        }
    }

    @Override
    public Result<HttpResponse> get() throws InterruptedException, ExecutionException {
        if (error != null)
            return Result.failure(error);
        return super.get();
    }

}
