package com.fospe.remember.ui.HomeScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.fospe.remember.R
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.custom_toolbar.*

class HomeActivity : AppCompatActivity() {
    lateinit var navController :NavController
    lateinit var toggle :ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toggle= ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu)
        toggle.syncState();
        navController =Navigation.findNavController(this,R.id.fragmentNav)
        NavigationUI.setupWithNavController(navigation_view,navController)

        //NavigationUI.setupActionBarWithNavController(this,navController,drawer_layout)

    }
}