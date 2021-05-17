package com.fospe.remember.ui.HomeScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.fospe.remember.Dagger.DaggerSharedPreferenceComponent
import com.fospe.remember.R
import com.fospe.remember.utility.SharedPref
import com.google.gson.Gson
import com.remember.api.models.registration.User
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.nav_header.view.*

class HomeActivity : AppCompatActivity() {
    lateinit var navController :NavController
    lateinit var toggle :ActionBarDrawerToggle

    private lateinit var sharedPref: SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        supportActionBar?.title="Remember"
        toggle= ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
        drawer_layout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu)
        toggle.syncState();
        navController =Navigation.findNavController(this,R.id.fragmentNav)
        NavigationUI.setupWithNavController(navigation_view,navController)
        val gson = Gson()
            // sharedPref = DaggerSharedPreferenceComponent.builder().sharePreferenceModule()

        sharedPref=SharedPref(this)
        var user = sharedPref.get<User>("user")

        Log.d("Shiva","user :"+user)


        navigation_view.getHeaderView(0).tvUserNameHead.text= "hello, "+user?.name


        //NavigationUI.setupActionBarWithNavController(this,navController,drawer_layout)

    }
}