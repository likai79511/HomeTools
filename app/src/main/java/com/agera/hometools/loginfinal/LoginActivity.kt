package com.agera.hometools.loginfinal

import android.app.Activity
import android.os.Bundle
import android.widget.EditText
import com.agera.hometools.R
import com.google.android.agera.Repositories
import com.google.android.agera.Result
import com.google.android.agera.Supplier

/**
 * Created by 43992639 on 2017/11/24.
 */
class LoginActivity : Activity() {


    var mEt_tel: EditText? = null
    var mEt_password: EditText? = null
    var mEt_confirm_password: EditText? = null
    var activeOnce:Result<String> = Result.absent()
    var tel:String = ""
    var password:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()
        initEvents()
    }

    private fun initEvents() {
        Repositories.repositoryWithInitialValue(Result.absent<String>())
                .observe()
                .onUpdatesPerLoop()
                .attemptGetFrom{activeOnce}
                .orSkip()
                .attemptGetFrom{LoginImp.instance().checkTel(mEt_tel)}
                .orSkip()
                .attemptTransform {
                    tel = it
                    LoginImp.instance().checkPassword(mEt_password)
                }
                .orSkip()
                .attemptTransform {
                    password = it
                    LoginImp.instance().checkConfirmPassword(mEt_confirm_password,password)
                }
                .orSkip()

    }

    private fun initViews() {
        mEt_tel = findViewById(R.id.et_tel) as EditText
        mEt_password = findViewById(R.id.et_password) as EditText
        mEt_confirm_password = findViewById(R.id.et_confirm_password) as EditText
    }


}