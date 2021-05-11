package com.fospe.remember.ui.members

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fospe.remember.R
import com.fospe.remember.utility.changeToolbarText
import kotlinx.android.synthetic.main.custom_toolbar.*

class MemberDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_member_details)
        changeToolbarText("Details", supportActionBar, toolbar)
    }
}