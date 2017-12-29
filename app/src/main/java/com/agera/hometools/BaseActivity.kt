package com.agera.hometools

import android.app.Activity
import com.google.android.agera.RepositoryCompilerStates
import com.google.android.agera.Result

/**
 * Created by mac on 2017/12/22.
 */
open class BaseActivity : Activity() {

    inline fun <F : RepositoryCompilerStates.RFlow<*, *, *>,T> F.typedResult(value:Class<T>): RepositoryCompilerStates.RFlow<Result<T>,T, *> = this as RepositoryCompilerStates.RFlow<Result<T>,T, *>


    override fun onStart() {
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}