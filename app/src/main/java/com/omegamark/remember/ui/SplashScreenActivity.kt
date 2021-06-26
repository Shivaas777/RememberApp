package com.omegamark.remember.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.OnCompleteListener
import com.omegamark.remember.R
import com.omegamark.remember.datastore.UserPreferences
import com.omegamark.remember.ui.HomeScreen.HomeActivity
import com.omegamark.remember.ui.login.LoginActivity
import com.omegamark.remember.utility.SharedPref
import com.omegamark.remember.utility.snack
import com.omegamark.remember.viewmodels.login.LoginViewModel
import com.omegamark.remember.viewmodels.login.LoginViewModelFactory
import com.remember.api.models.registration.User
import com.remember.api.repository.APIRepository
import kotlinx.android.synthetic.main.activity_splash_screen.*
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.messaging.FirebaseMessaging


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




    }


    override fun onStart() {
        super.onStart()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("shiva", "Fetching FCM registration token failed", task.exception)
                sharedPreferences.putString("","firebase_token")
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast

            Log.d("shiva","token" +token)
            sharedPreferences.putString(token,"firebase_token")
        })
    }


    override fun onResume() {
        super.onResume()
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
            Log.d("shiva","token"+sharedPreferences.getString("firebase_token"))
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            finish()

        }, 5000) // 3000 is the delayed time in milliseconds.*/
    }
    fun autoLogin()
    {
        val params = HashMap<String, String>()

        params["mobile"] = user?.mobile.toString()
        params["password"] = sharedPreferences.getString("password")
        params["device_type"] = "1"
        params["device_token"] = sharedPreferences.getString("firebase_token")
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
