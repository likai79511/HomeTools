package com.agera.hometools.login

import android.view.View
import com.google.android.agera.BaseObservable

/**
 * Created by Agera on 2017/11/25 0025.
 */
class ClickObservable : BaseObservable(), View.OnClickListener {
    override fun onClick(v: View?) {
        dispatchUpdate()
    }
}