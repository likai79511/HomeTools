package com.agera.hometools.push

import android.content.Context
import cn.jpush.android.api.JPushInterface
import com.agera.hometools.core.TaskDriver
import com.agera.hometools.network.Restful
import com.agera.hometools.utils.CommonUtils
import com.agera.hometools.utils.Constants

/**
 * Created by mac on 2017/12/14.
 */
class PushImp private constructor():PushInter{


    companion object {
        private var utils = PushImp()

        fun instance(): PushImp {
            return utils
        }
    }


    override fun setPushAccount(ctx: Context, account: String) {

        if (JPushInterface.isPushStopped(ctx))
            JPushInterface.resumePush(ctx)

        JPushInterface.setAlias(ctx,System.currentTimeMillis().toInt(), account)
    }


    override fun requireLocationByAlias(alisa: String) {
        TaskDriver.instance().mCore.submit(Restful.instance().sendMessage(MessageFactory.instance().createPushMessage(Constants.MESSAGE_LOCATION,alisa,CommonUtils.instance().getData(Constants.USERNAME,"").toString())))
    }

    override fun requireLocationByTag(tag: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

