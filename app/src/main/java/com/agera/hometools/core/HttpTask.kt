package com.agera.hometools.core

import com.google.android.agera.Result
import com.google.android.agera.net.HttpResponse
import java.util.concurrent.FutureTask

/**
 * Created by Administrator on 2017/11/2 0002.
 */
class HttpTask : FutureTask<Result<HttpResponse>>(val cb2:Callable){

}