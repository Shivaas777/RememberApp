package com.fospe.remember

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.fospe.remember.internetchange.DetectInternet
import com.fospe.remember.utility.snack
import kotlinx.android.synthetic.main.event_item.*

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
                view.snack("You are online now")
            }
            else {
                view.snack("You are offline now")
            }
        })
    }
}