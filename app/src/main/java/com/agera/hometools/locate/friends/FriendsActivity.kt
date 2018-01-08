package com.agera.hometools.locate.friends

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import com.agera.hometools.BaseActivity
import com.agera.hometools.R
import com.agera.hometools.common.RefreshListener
import com.agera.hometools.core.TaskDriver
import com.agera.hometools.utils.CommonUtils
import com.google.android.agera.Repositories
import com.google.android.agera.Repository
import com.google.android.agera.Result
import com.google.android.agera.Updatable

class FriendsActivity : BaseActivity(), Updatable {


    var mSwipe: SwipeRefreshLayout? = null
    var mRv: RecyclerView? = null
    var mRep: Repository<Result<String>>? = null
    var mRefreshlistener: RefreshListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)

        initView()
        initEvents()

    }

    fun initView() {
        mSwipe = findViewById(R.id.swipe) as SwipeRefreshLayout
        mRv = findViewById(R.id.rv) as RecyclerView
    }

    fun initEvents() {
        mRefreshlistener = RefreshListener()
        mSwipe?.setOnRefreshListener(mRefreshlistener)

        mRep = Repositories.repositoryWithInitialValue(Result.absent<String>())
                .observe(mRefreshlistener)
                .onUpdatesPerLoop()
                .goTo(TaskDriver.instance().mCore)
                .typedResult(String::class.java)
                .thenTransform {

                }
                .notifyIf { _, v2 ->
                    if (v2.failed()) {
                        CommonUtils.instance().showShortMessage(mSwipe!!, "网络异常")
                    }
                    v2.succeeded()
                }
                .compile()
        mRep?.addUpdatable(this)

    }


    override fun update() {
        var result = mRep!!.get().get()
    }

    override fun onDestroy() {
        super.onDestroy()
        mRep?.removeUpdatable(this)
    }
}
