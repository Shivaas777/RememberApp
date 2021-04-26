package com.fospe.remember.ui.HomeScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.fospe.remember.R
import com.fospe.remember.datastore.UserPreferences
import com.fospe.remember.utility.SharedPref
import com.google.gson.Gson
import com.remember.api.models.registration.User
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.custom_toolbar.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.nav_header.view.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.json.JSONObject

class HomeActivity : AppCompatActivity() {
    lateinit var navController :NavController
    lateinit var toggle :ActionBarDrawerToggle

    private lateinit var sharedPref: SharedPref
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

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
        sharedPref = SharedPref(this)

        var user = sharedPref.get<User>("user")

        Log.d("Shiva","user :"+user)


        navigation_view.getHeaderView(0).tvUserNameHead.text= "hello, "+user?.name


        //NavigationUI.setupActionBarWithNavController(this,navController,drawer_layout)

    }
}