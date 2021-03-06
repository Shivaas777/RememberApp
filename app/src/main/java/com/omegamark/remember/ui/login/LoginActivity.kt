package com.omegamark.remember.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.omegamark.remember.R
import com.omegamark.remember.ui.HomeScreen.HomeActivity
import com.omegamark.remember.ui.register.RegisterActivity
import com.omegamark.remember.utility.*
import com.omegamark.remember.viewmodels.login.LoginViewModel
import com.omegamark.remember.viewmodels.login.LoginViewModelFactory
import com.remember.api.repository.APIRepository
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_splash_screen.*

class LoginActivity : AppCompatActivity() {

    private lateinit var apiRepository: APIRepository
    private lateinit var loginViewModelFactory: LoginViewModelFactory
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var sharedPreferences: SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (supportActionBar != null)
            supportActionBar?.hide()


        sharedPreferences= SharedPref(this)
        apiRepository= APIRepository()
        loginViewModelFactory= LoginViewModelFactory(apiRepository)
        loginViewModel=ViewModelProvider(this,loginViewModelFactory).get(LoginViewModel::class.java)
        observeloginResponse()
        btn_next.setOnClickListener {
            /*if(checkForBiometrics(this)) {
                showDialogConfirmation(this, { if (it.equals("positive", true)) finish() })
            }*/

            Log.d("shiva_login","token"+sharedPreferences.getString("firebase_token"))
            showProgressDialog(this,"Logging in..")

            var params = HashMap<String,String>()
            params["mobile"] = etMobile.text.toString()
            params["password"] = etPassword.text.toString()
            params["device_type"] = "1"
            params["device_token"] = sharedPreferences.getString("firebase_token")
            loginViewModel.getLoginResponse(params)
        }
        btn_cancel.setOnClickListener { finish() }
        btn_signUp.setOnClickListener {
            val intent:Intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }
        etPassword.afterTextChangedChangeButtonImage{ changeBG(it,etMobile,btn_next) }
        etMobile.afterTextChangedChangeButtonImage{ changeBG(it,etPassword,btn_next) }

    }

    fun observeloginResponse()
    {
        loginViewModel.loginresponse.observe(this, Observer {

            hideProgreeDialog()

            when (it.isSuccess) {
                true -> {


                    sharedPreferences.put(it.response,"user")
                    sharedPreferences.putString(etPassword.text.toString(),"password")
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                false -> {

                    splashImage.snack(it.message)

                }
            }
        })
    }
}