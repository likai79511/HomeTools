package com.agera.hometools

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.agera.hometools.locate.LocateActivity
import com.agera.hometools.push.PushImp
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by mac on 2017/10/23.
 */
class MainActivity:Activity(), View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_locate.setOnClickListener(this)
        btn_search_way.setOnClickListener(this)
        btn_family.setOnClickListener(this)
        btn_kd.setOnClickListener(this)
        btn_setting.setOnClickListener(this)




    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_locate -> startActivity(Intent(this, LocateActivity::class.java))
            R.id.btn_family -> {}
            R.id.btn_kd -> {}
            R.id.btn_setting -> {
                PushImp.instance().requireLocationByAlias("18291427145")
            }
        }
    }
}