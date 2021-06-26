package com.omegamark.remember.ui.register

import android.os.Bundle
import android.view.WindowManager
import com.omegamark.remember.BaseActivity
import com.omegamark.remember.R

class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)
        if (supportActionBar != null)
            supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)


    }
}