package com.agera.hometools.loginfinal

import android.widget.EditText
import com.google.android.agera.Result

/**
 * Created by 43992639 on 2017/11/24.
 */
interface LoginInter {
    fun checkTel(view:EditText?):Result<String>

    fun checkPassword(view:EditText?):Result<String>

    fun checkConfirmPassword(view:EditText?,password:String):Result<String>

    fun login(tel:String,password:String):Result<String>
}