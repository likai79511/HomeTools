package com.agera.hometools.locate.friends

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.util.Log
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


    lateinit var mSwipe: SwipeRefreshLayout
    var mRv: RecyclerView? = null
    lateinit var mRep: Repository<Result<List<*>>>
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

        mSwipe.setRefreshing(true)

        mRep = Repositories.repositoryWithInitialValue(Result.absent<List<String>>())
                .observe(mRefreshlistener)
                .onUpdatesPerLoop()
                .goTo(TaskDriver.instance().mCore)
                .typedResult(List::class.java)
                .thenGetFrom {
                    var friendlist = FriendsImp.instance().getFriends();
                    if (friendlist == null || friendlist.size == 0) {
                        TaskDriver.instance().mainHandler.post {
                            mSwipe.isRefreshing = false
                        }
                        Result.failure()
                    } else {
                        Result.success(friendlist)
                    }
                }
                .notifyIf { _, v2 ->
                    if (v2.failed()) {
                        CommonUtils.instance().showShortMessage(mSwipe!!, "没有数据")
                    }
                    v2.succeeded()
                }
                .compile()
        mRep?.addUpdatable(this)

    }


    override fun update() {
        var result = mRep!!.get().get()
        mSwipe.setRefreshing(false)
        Log.e("---", "----update----")
        var list = mRep.get().get()
        list.forEach {
            Log.e("---", "--element::$it")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        mRep?.removeUpdatable(this)
    }
}
