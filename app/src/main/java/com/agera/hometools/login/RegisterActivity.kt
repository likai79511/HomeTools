package com.agera.hometools.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.agera.hometools.BaseActivity
import com.agera.hometools.MyApp
import com.agera.hometools.R
import com.agera.hometools.common.ClickObservable
import com.agera.hometools.core.TaskDriver
import com.agera.hometools.utils.CommonUtils
import com.google.android.agera.Repositories
import com.google.android.agera.Repository
import com.google.android.agera.Result
import com.google.android.agera.Updatable

/**
 * Created by Agera on 2017/11/25 0025.
 */
class RegisterActivity : BaseActivity(), Updatable {

    var mEt_tel: EditText? = null
    var mEt_password: EditText? = null
    var mEt_confirm_password: EditText? = null
    var activeOnce: Result<String> = Result.absent()
    var tel: String = ""
    var password: String = ""
    var mRep: Repository<Result<String>>? = null
    var loginObservable: ClickObservable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initViews()
        initEvents()
    }

    private fun initEvents() {
        loginObservable = ClickObservable()
        findViewById(R.id.btn_register).setOnClickListener { view ->
            activeOnce = Result.success("start repository")
            (MyApp.instance().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(view.windowToken, 0)
            loginObservable!!.onClick(view)
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
                .attemptTransform {
                    password = it
                    LoginImp.instance().checkConfirmPassword(mEt_confirm_password, password)
                }
                .orSkip()
                .goTo(TaskDriver.instance().mCore)
                .typedResult(String::class.java)
                .thenTransform {
                    CommonUtils.instance().showShortMessage(mEt_tel!!, "正在注册...")
                    LoginImp.instance().register(tel, password)
                }
                .notifyIf { _, v2 ->
                    if (v2.failed())
                        CommonUtils.instance().showShortMessage(mEt_tel!!, "注册失败，该账号已经存在...")
                    v2.succeeded()
                }
                .compile()
        mRep!!.addUpdatable(this)
    }

    private fun initViews() {
        mEt_tel = findViewById(R.id.et_tel) as EditText
        mEt_password = findViewById(R.id.et_password) as EditText
        mEt_confirm_password = findViewById(R.id.et_confirm_password) as EditText
    }

    override fun update() {
        CommonUtils.instance().showShortMessage(mEt_tel!!, "注册成功，即将跳转登陆页面...")
        TaskDriver.instance().mainHandler.postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
        },1500)

    }

    override fun onDestroy() {
        super.onDestroy()
        mRep?.let { it.removeUpdatable(this) }
    }
}