package com.agera.hometools.push

import android.content.Context
import cn.jpush.android.api.JPushInterface

/**
 * Created by mac on 2017/12/14.
 */
class PushUtils private constructor(){

    companion object {
        private var utils = PushUtils()

        fun instance(): PushUtils {
            return utils
        }
    }


    fun setPushAccount(ctx: Context, account: String) {

        if (JPushInterface.isPushStopped(ctx))
            JPushInterface.resumePush(ctx)

        JPushInterface.setAlias(ctx,System.currentTimeMillis().toInt(), account)
    }
}

