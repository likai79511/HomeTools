package com.agera.hometools

import android.app.Activity
import android.os.Bundle
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.SaveCallback

/**
 * Created by Administrator on 2017/11/26 0026.
 */
class PushActivity:Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.push_layout)

       var o1:AVObject = AVObject("testObject")
        o1.put("agera","handsome")
        o1.saveInBackground(object: SaveCallback() {
            override fun done(e: AVException?) {
                e?.let{

                }
            }

        })
    }
}