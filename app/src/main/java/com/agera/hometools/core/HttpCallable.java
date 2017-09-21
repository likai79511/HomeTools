package com.agera.hometools.core;

import com.google.android.agera.Result;
import com.google.android.agera.net.HttpFunctions;
import com.google.android.agera.net.HttpRequest;
import com.google.android.agera.net.HttpResponse;

import java.util.concurrent.Callable;

/**
 * Created by Administrator on 2017/9/21.
 */

public class HttpCallable implements Callable<Result<HttpResponse>> {

    private HttpRequest request = null;

    public HttpCallable(HttpRequest req) {
        request = req;
    }

    @Override
    public Result<HttpResponse> call() throws Exception {
        if (request == null)
            return Result.failure(new Throwable("request is null"));
        return HttpFunctions.httpFunction().apply(request);
    }
}
