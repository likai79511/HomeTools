package com.agera.hometools.utils;

import android.support.design.widget.Snackbar;
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
    private CommonUtils(){
    }

    public static CommonUtils instance(){
        synchronized (CommonUtils.class){
            if (mUtils == null){
                mUtils = new CommonUtils();
            }
        }
        return mUtils;
    }

    public void showMessage(String msg,View view,int type){
        if (view == null) {
            Toast.makeText(MyApp.getInstance(),msg,type).show();
        } else {
           Snackbar.make(view,msg,type).show();
        }
    }

}
