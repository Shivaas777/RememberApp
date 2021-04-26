package com.fospe.remember.ui

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.fospe.remember.R
import com.fospe.remember.datastore.UserPreferences
import com.fospe.remember.ui.HomeScreen.HomeActivity
import com.fospe.remember.ui.register.RegisterActivity
import com.fospe.remember.utility.SharedPref
import com.fospe.remember.utility.snack
import com.fospe.remember.viewmodels.login.LoginViewModel
import com.fospe.remember.viewmodels.login.LoginViewModelFactory
import com.fospe.remember.viewmodels.verification.VerificationViewModel
import com.fospe.remember.viewmodels.verification.VerificationViewModelFactory
import com.google.gson.Gson
import com.remember.api.models.registration.User
import com.remember.api.repository.APIRepository
import kotlinx.android.synthetic.main.activity_splash_screen.*
import kotlinx.android.synthetic.main.fragment_mobile_number.view.*
import kotlinx.android.synthetic.main.fragment_name.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var userPreferences: UserPreferences
    private lateinit var sharedPreferences: SharedPref
    private var user: User?=null
    lateinit var apiRepository: APIRepository
    lateinit var loginViewModelFactory: LoginViewModelFactory
    lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        apiRepository= APIRepository()
        loginViewModelFactory= LoginViewModelFactory(apiRepository)
        loginViewModel = ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel::class.java)
        userPreferences = UserPreferences(this)
        sharedPreferences= SharedPref(this)
        user = sharedPreferences.get<User>("user")
        observeloginResponse()
        if(user==null)
        {
            withoutLogin()
        }
        else
        {
            autoLogin()
        }



    }

    fun withoutLogin()
    {

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed({
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
            finish()
        }, 3000) // 3000 is the delayed time in milliseconds.*/
    }
    fun autoLogin()
    {
        val params = HashMap<String, String>()

        params["mobile"] = user?.mobile.toString()
        params["password"] = sharedPreferences.getString("password")
        params["device_type"] = "1"
        params["device_token"] = "qqwee"
        loginViewModel.getLoginResponse(params)

    }

    fun observeloginResponse()
    {
        loginViewModel.loginresponse.observe(this, Observer {

            when (it.isSuccess) {
                true -> {


                    sharedPreferences.put(it.response,"user")
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
