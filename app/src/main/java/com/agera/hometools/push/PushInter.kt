package com.agera.hometools.push

import android.content.Context

/**
 * Created by mac on 2017/12/15.
 */
interface PushInter {

    //set push Alias
    fun setPushAccount(ctx: Context, alias: String)

    //require location by alisa
    fun requireLocationByAlias(alisa:String)

    //require location bt tag
    fun requireLocationByTag(tag:String)

}