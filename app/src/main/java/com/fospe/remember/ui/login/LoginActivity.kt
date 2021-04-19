package com.fospe.remember.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.fospe.remember.R
import com.fospe.remember.utility.afterTextChangedChangeButtonImage
import com.fospe.remember.utility.changeBG
import com.fospe.remember.utility.checkForBiometrics
import com.fospe.remember.utility.showDialogConfirmation
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (supportActionBar != null)
            supportActionBar?.hide()
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)


        btn_next.setOnClickListener {
            if(checkForBiometrics(this)) {
                showDialogConfirmation(this, { if (it.equals("positive", true)) finish() })
            }
        }
        btn_cancel.setOnClickListener { finish() }
        etPassword.afterTextChangedChangeButtonImage{ changeBG(it,etMobile,btn_next) }
        etMobile.afterTextChangedChangeButtonImage{ changeBG(it,etPassword,btn_next) }

    }


}