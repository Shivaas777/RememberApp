package com.fospe.remember.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.fospe.remember.BaseActivity
import com.fospe.remember.R

class RegisterActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)
        if (supportActionBar != null)
            supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)


    }
}