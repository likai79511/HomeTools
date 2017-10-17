package com.agera.hometools.utils;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.agera.hometools.MyApp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mac on 2017/10/17.
 */

public class CommonUtils {

    private static CommonUtils mUtils = null;
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

    private Map<View,Snackbar> mSnackbars = new HashMap<>();
    private Snackbar snackbar = null;
    public void showMessage(String msg,View view,int type){
        if (view == null) {
            Toast.makeText(MyApp.getInstance(),msg,type).show();
        } else {
            if (mSnackbars.containsKey(view)){
                mSnackbars.get(view).setText(msg).setDuration(type).show();
            }else{
                snackbar = Snackbar.make(view,msg,type);
                mSnackbars.put(view,snackbar);
                snackbar.show();
            }
        }
    }

    public void dismissSnackBar(View...views){
        for (View v:views){
            if (mSnackbars.containsKey(v)){
                mSnackbars.get(v).dismiss();
            }
        }
    }
}
