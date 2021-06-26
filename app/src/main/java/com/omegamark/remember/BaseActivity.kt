package com.omegamark.remember

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.omegamark.remember.internetchange.DetectInternet
import com.omegamark.remember.utility.showError
import com.omegamark.remember.utility.showWarning

open class BaseActivity : AppCompatActivity() {

    private lateinit var intenetChange :DetectInternet
    lateinit var view_:View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        view_= findViewById(android.R.id.content)
        intenetChange= DetectInternet(application)
        intenetChange.observe(this, Observer { isAvailable->

            if(isAvailable)
            {
                showWarning(this,"Internet Connection","Connection is available now")
            }
            else {
                showError(this,"Internet Connection","Connection is not available")
            }
        })
    }
}