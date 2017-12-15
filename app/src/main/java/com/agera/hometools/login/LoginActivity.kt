package com.agera.hometools.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.agera.hometools.MainActivity
import com.agera.hometools.MyApp
import com.agera.hometools.R
import com.agera.hometools.utils.CommonUtils
import com.agera.hometools.utils.Constants
import com.agera.hometools.push.PushImp
import com.google.android.agera.Repositories
import com.google.android.agera.Repository
import com.google.android.agera.Result
import com.google.android.agera.Updatable

/**
 * Created by 43992639 on 2017/11/24.
 */
class LoginActivity : Activity(), Updatable {

    var mEt_tel: EditText? = null
    var mEt_password: EditText? = null
    var activeOnce: Result<String> = Result.failure()
    var tel: String = ""
    var password: String = ""
    var mRep: Repository<Result<String>>? = null
    var loginObservable: ClickObservable? = null
    var tv_register: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()
        initEvents()
    }

    private fun initEvents() {
        loginObservable = ClickObservable()
        findViewById(R.id.btn_Login).setOnClickListener { view ->
            activeOnce = Result.success("start repository")
            (MyApp.instance().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
            loginObservable!!.onClick(view)
        }
        tv_register?.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        mRep = Repositories.repositoryWithInitialValue(Result.absent<String>())
                .observe(loginObservable)
                .onUpdatesPerLoop()
                .attemptGetFrom { activeOnce }
                .orSkip()
                .attemptGetFrom { LoginImp.instance().checkTel(mEt_tel) }
                .orSkip()
                .attemptTransform {
                    tel = it
                    LoginImp.instance().checkPassword(mEt_password)
                }
                .orSkip()
                .thenTransform {
                    password = it
                    CommonUtils.instance().showShortMessage(mEt_tel!!, "正在登录...")
                    LoginImp.instance().login(tel, password)
                }
                .notifyIf { _, v2 ->
                    if (v2.failed())
                        CommonUtils.instance().showShortMessage(mEt_tel!!, "登陆失败...")
                    v2.succeeded()
                }
                .compile()

        mRep?.let { it.addUpdatable(this) }
    }

    private fun initViews() {
        mEt_tel = findViewById(R.id.et_tel) as EditText
        mEt_password = findViewById(R.id.et_password) as EditText
        tv_register = findViewById(R.id.tv_register) as TextView
        tv_register!!.paintFlags = Paint.UNDERLINE_TEXT_FLAG
    }

    override fun onDestroy() {
        super.onDestroy()
        mRep?.let {
            it.removeUpdatable(this)
        }
    }

    override fun update() {
        //auto remember account
        CommonUtils.instance().saveData(Constants.USERNAME, tel)
        CommonUtils.instance().saveData(Constants.PASSWORD, password)
        //refresh push
        PushImp.instance().setPushAccount(this,tel)
        //start main page
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

}