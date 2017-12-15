package com.agera.hometools

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import android.widget.TextView
import com.agera.hometools.push.PushImp

class TestPush : AppCompatActivity() {

    var lv:LinearLayout ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_push)

        lv = findViewById(R.id.lv) as LinearLayout

        MyApp.instance().activity = this

        PushImp.instance().setPushAccount(this,"18291427145")
    }

    fun addMsg(content:String){
        var tv = TextView(this)
        tv.apply {
            setTextColor(Color.BLACK)
            setTextSize(16f)
            setText(content)
        }
        lv?.addView(tv)
    }


}
