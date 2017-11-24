package com.agera.hometools.utils

import android.content.Context
import android.support.design.widget.Snackbar
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.agera.hometools.MyApp
import com.google.gson.Gson

/**
 * Created by mac on 2017/11/7.
 */
class CommonUtils private constructor() {
    var gson: Gson = Gson()
    var MESSAGE_DURATION_SHORT: Int = 1
    var MESSAGE_DURATION_LONG: Int = 2

    companion object {
        private var instance: CommonUtils = CommonUtils()
        fun instance(): CommonUtils {
            return instance
        }
    }

    fun showShortMessage(payload: View, message: String) {
        if (payload == null) {
            Toast.makeText(MyApp.getInstance(), message, Toast.LENGTH_SHORT).show()
        } else {
            Snackbar.make(payload, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    fun showLongMessage(payload: View, message: String) {
        if (payload == null) {
            Toast.makeText(MyApp.getInstance(), message, Toast.LENGTH_LONG).show()
        } else {
            Snackbar.make(payload, message, Snackbar.LENGTH_LONG).show()
        }
    }

    /**
     * Save data to SD card
     */
    fun <T> saveData(key: String, value: T): Boolean {
        val edit = MyApp.getInstance().getSharedPreferences(Constants.DATASTORE, Context.MODE_PRIVATE).edit()
        if (value is String) {
            return edit.putString(key, value).commit()
        } else if (value is Boolean) {
            return edit.putBoolean(key, value).commit()
        } else if (value is Int) {
            return edit.putInt(key, value).commit()
        } else if (value is Long) {
            return edit.putLong(key, value).commit()
        } else if (value is Float) {
            return edit.putFloat(key, value).commit()
        }
        return false
    }

    /**
     * Get Data from SD card

     * @param key
     * *
     * @param dValue
     * *
     * @return
     */
    fun getData(key: String, dValue: Any): Any? {
        val sp = MyApp.getInstance().getSharedPreferences(Constants.DATASTORE, Context.MODE_PRIVATE)
        if (dValue is String) {
            return sp.getString(key, dValue)
        } else if (dValue is Boolean) {
            return sp.getBoolean(key, dValue)
        } else if (dValue is Int) {
            return sp.getInt(key, dValue)
        } else if (dValue is Float) {
            return sp.getFloat(key, dValue)
        } else if (dValue is Long) {
            return sp.getLong(key, dValue)
        }
        return null
    }

    fun clearData(vararg keys: String): Boolean? {
        try {
            for (key in keys) {
                MyApp.getInstance().getSharedPreferences(Constants.DATASTORE, Context.MODE_PRIVATE).edit().remove(key).commit()
            }
        } catch (e: Exception) {
            Log.e("---", "---clearData encounter error:" + e.message)
            return false
        }
        return true
    }


    fun checkTel(tel: String): Boolean = !TextUtils.isEmpty(tel) && tel.length == 11

    fun checkPassword(password: String): Boolean = !TextUtils.isEmpty(password) && password.length >= 6

    fun checkConfirmPassword(pass1:String,pass2:String):Boolean = !TextUtils.isEmpty(pass1) && !TextUtils.isEmpty(pass2) && pass1.equals(pass2)
}