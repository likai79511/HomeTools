package com.agera.hometools.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.agera.hometools.MyApp;
import com.google.gson.Gson;

/**
 * Created by mac on 2017/10/17.
 */

public class CommonUtils {

    private static CommonUtils mUtils = null;
    public Gson gson = new Gson();

    private CommonUtils() {
    }

    public static CommonUtils instance() {
        synchronized (CommonUtils.class) {
            if (mUtils == null) {
                mUtils = new CommonUtils();
            }
        }
        return mUtils;
    }

    public void showMessage(String msg, View view, int type) {
        if (view == null) {
            Toast.makeText(MyApp.getInstance(), msg, type).show();
        } else {
            Snackbar.make(view, msg, type).show();
        }
    }

    /**
     * Save data to SD card
     *
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean saveData(String key, T value) {
        SharedPreferences.Editor edit = MyApp.getInstance().getSharedPreferences(Constants.DATASTORE, Context.MODE_PRIVATE).edit();
        if (value instanceof String) {
            return edit.putString(key, (String) value).commit();
        } else if (value instanceof Boolean) {
            return edit.putBoolean(key, (Boolean) value).commit();
        } else if (value instanceof Integer) {
            return edit.putInt(key, (Integer) value).commit();
        } else if (value instanceof Long) {
            return edit.putLong(key, (Long) value).commit();
        } else if (value instanceof Float) {
            return edit.putFloat(key, (Float) value).commit();
        }
        return false;
    }

    /**
     * Get Data from SD card
     *
     * @param key
     * @param dValue
     * @return
     */
    public Object getData(String key, Object dValue) {
        SharedPreferences sp = MyApp.getInstance().getSharedPreferences(Constants.DATASTORE, Context.MODE_PRIVATE);
        if (dValue instanceof String) {
            return sp.getString(key, (String) dValue);
        } else if (dValue instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) dValue);
        } else if (dValue instanceof Integer) {
            return sp.getInt(key, (Integer) dValue);
        } else if (dValue instanceof Float) {
            return sp.getFloat(key, (Float) dValue);
        } else if (dValue instanceof Long) {
            return sp.getLong(key, (Long) dValue);
        }
        return null;
    }

    public Boolean clearData(String... keys) {
        try {
            for (String key : keys) {
                MyApp.getInstance().getSharedPreferences(Constants.DATASTORE, Context.MODE_PRIVATE).edit().remove(key).commit();
            }
        } catch (Exception e) {
            Log.e("---", "---clearData encounter error:" + e.getMessage());
            return false;
        }
        return true;
    }

}
