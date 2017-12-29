package com.agera.hometools.locate.friends

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.agera.hometools.BaseActivity
import com.agera.hometools.R
import com.agera.hometools.network.Restful

class FriendsActivity : BaseActivity() {

    var mRv: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        mRv = findViewById(R.id.rv) as RecyclerView

        pullLatestData()
    }

    fun pullLatestData(){
        Restful.instance()
    }



}
