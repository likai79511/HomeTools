package com.agera.hometools.login

import android.widget.EditText
import com.google.android.agera.Result

/**
 * Created by Agera on 2017/11/24.
 */
interface LoginInter {
    fun checkTel(view:EditText?):Result<String>

    fun checkPassword(view:EditText?):Result<String>

    fun checkConfirmPassword(view:EditText?,password:String):Result<String>

    fun login(tel:String,password:String):Result<String>

    fun register(tel:String,password:String):Result<String>
}