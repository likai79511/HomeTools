package com.agera.hometools.login

import android.app.Activity
import android.os.Bundle
import android.util.Pair
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.agera.hometools.R
import com.google.android.agera.*
import com.google.android.agera.Function
import com.google.android.agera.net.HttpResponse
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by Administrator on 2017/11/14 0014.
 */
class RegisterActivity : Activity(), Updatable {

    var mEt_tel: EditText? = null
    private var mEt_password: EditText? = null
    private var mEt_confirm_password: EditText? = null
    private var tel: String? = null
    private var password: String? = null
    private var confirm_password: String? = null
    private var mRep: Repository<Result<HttpResponse>>? = null
    private var mOb: OnClickListenerObservable? = null
    private val activeOnce = AtomicBoolean(false)
    private var mBtn_register: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        initEvents()

    }

    private fun initEvents() {

        mOb = OnClickListenerObservable()
        mBtn_register?.let { it.setOnClickListener(mOb) }
        mRep = Repositories.repositoryWithInitialValue(Result.absent<HttpResponse>())
                .observe(mOb)
                .onUpdatesPerLoop()
                .check(Predicate { activeOnce.getAndSet(true) })
                .orSkip()
                .getFrom(Supplier { tel = mEt_tel!!.text.toString().trim() })
                .check(Predicate { LoginFunctionsImp.instance().checkTel(mEt_tel!!)!!.apply(tel!!) })
                .orSkip()
                .getFrom(Supplier { password = mEt_password?.text?.toString()?.trim() })
                .check(Predicate { LoginFunctionsImp.instance().checkTel(mEt_password!!)!!.apply(password!!) })
                .orSkip()
                .getFrom(Supplier { confirm_password = mEt_confirm_password?.text?.toString()?.trim() })
                .check(Predicate { LoginFunctionsImp.instance().checkConfirmPassword(mEt_confirm_password!!)!!.apply(confirm_password!!) })
                .orSkip()
                .thenTransform(Function<>)

    }

    private fun initViews() {
        mEt_tel = findViewById(R.id.et_tel) as EditText
        mEt_password = findViewById(R.id.et_password) as EditText
        mEt_confirm_password = findViewById(R.id.et_confirm_password) as EditText
        mBtn_register = findViewById(R.id.btn_register) as Button
    }

    override fun update() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDestroy() {
        super.onDestroy()
        mRep.removeUpdatable(this)
    }

    class OnClickListenerObservable : BaseObservable(), View.OnClickListener {
        override fun onClick(p0: View?) {
            dispatchUpdate()
        }
    }
}