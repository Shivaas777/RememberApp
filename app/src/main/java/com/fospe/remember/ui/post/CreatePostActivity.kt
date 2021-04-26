package com.fospe.remember.ui.post

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fospe.remember.R
import com.fospe.remember.utility.changeToolbarText
import kotlinx.android.synthetic.main.custom_toolbar.*

class CreatePostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        setSupportActionBar(toolbar)
        changeToolbarText("Create", supportActionBar, toolbar)
    }
}