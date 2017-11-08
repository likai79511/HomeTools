package com.agera.hometools.login

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.agera.hometools.R
import com.google.android.agera.*
import com.google.android.agera.net.HttpResponse
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Created by mac on 2017/10/23.
 */
class LoginActivity : Activity(), Updatable {


    var mEt_tel: EditText? = null
    var mEt_password: EditText? = null
    var mBtn_login: Button? = null
    var mOb: OnClickListenerObservable? = null
    var mRep: Repository<Result<HttpResponse>>? = null
    var tel: String? = null
    var password: String? = null
    val activeOnce = AtomicBoolean(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initEvents()
    }


    fun initView() {
        mEt_tel = R.id.et_tel as EditText
        mEt_password = R.id.et_password as EditText
        mBtn_login = R.id.btn_Login as Button
    }

    fun initEvents() {

        mOb = OnClickListenerObservable()
        mBtn_login?.setOnClickListener { mOb }

//        return tel = if (mEt_tel == null) null else if (mEt_tel.getText() == null) null else mEt_tel.getText().toString().trim()
        Supplier { }
        Repositories.repositoryWithInitialValue(Result.absent<HttpResponse>())
                .observe()
                .onUpdatesPerLoop()
                .check { o -> activeOnce.getAndSet(true) }
                .orSkip()
                .getFrom{ tel = "123" }    //mEt_tel?.text?.toString()?.trim()

//                .check{ s1 -> LoginFunctionsImp.instance().checkTel(mEt_tel).apply(s1)}



        /*mRep = Repositories.repositoryWithInitialValue(Result.absent<HttpResponse>())
                .check { s -> LoginFunctionsImp.instance().checkTel(mEt_tel).apply(s) }
                .orSkip()
                .getFrom<String> { password = if (mEt_password == null) null else if (mEt_password.getText() == null) null else mEt_password.getText().toString().trim { it <= ' ' } }
                .check { s -> LoginFunctionsImp.instance().checkPassword(mEt_password).apply(s) }
                .orSkip()
                .thenTransform { s -> LoginFunctionsImp.instance().login(null, mBtn_login).apply(Pair.create<String, String>(tel, password)) }
                .notifyIf { response1, response2 -> LoginFunctionsImp.instance().checkLogin(mBtn_login).apply(response2) }
                .compile()
        activeOnce.set(false)
        mRep.addUpdatable(this)*/
    }

    override fun update() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    internal inner class OnClickListenerObservable : BaseObservable(), View.OnClickListener {
        override fun onClick(v: View) {
            dispatchUpdate()
        }
    }
}