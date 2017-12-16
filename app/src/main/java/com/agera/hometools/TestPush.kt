package com.agera.hometools

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import android.widget.TextView
import com.agera.hometools.push.PushImp
import kotlinx.android.synthetic.main.activity_test_push.*

class TestPush : AppCompatActivity() {

    var lv:LinearLayout ?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_push)

        lv = findViewById(R.id.lv) as LinearLayout

        btn.setOnClickListener{
            PushImp.instance().requireLocationByAlias("12345678901")
        }

        MyApp.instance().activity = this

        PushImp.instance().setPushAccount(this,"18291427145")

//        startService(LocationService(this,))
    }

    fun addMsg(content:String){
        var tv = TextView(this)
        tv.apply {
            setTextColor(Color.BLACK)
            textSize = 16f
            text = content
        }
        lv?.addView(tv)
    }


}
