package com.agera.hometools.core;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.agera.Receiver;
import com.google.android.agera.Result;
import com.google.android.agera.net.HttpRequest;
import com.google.android.agera.net.HttpResponse;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Created by Administrator on 2017/9/20.
 */

public class HttpTask extends FutureTask<Result<HttpResponse>> {

    private boolean isLocked = false;

    private Exception error = null;

    private HttpRequest request = null;

    public HttpTask(@NonNull Callable<Result<HttpResponse>> callable) {
        super(callable);
    }

    public HttpTask(@NonNull Runnable runnable, Result<HttpResponse> result) {
        super(runnable, result);
    }


    @Override
    protected void done() {
        if (isLocked) {
            TaskDriver.instance().mControl.release();
            isLocked = false;
        }
        try {
            get()
                    .ifFailedSendTo(new Receiver<Throwable>() {
                        @Override
                        public void accept(@NonNull Throwable value) {
                            Log.e("---", "--error-01:" + value.getMessage());
                        }
                    })
                    .ifSucceededSendTo(new Receiver<HttpResponse>() {
                        @Override
                        public void accept(@NonNull HttpResponse value) {
                            Log.e("---", "--result:" + value.getResponseMessage());
                        }
                    });
        } catch (Exception e) {
            Log.e("---", "--error-02:" + e.getMessage());
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
