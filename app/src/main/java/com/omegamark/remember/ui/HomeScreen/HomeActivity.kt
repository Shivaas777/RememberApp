package com.omegamark.remember.ui.HomeScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.omegamark.remember.R
import com.omegamark.remember.ui.QRCodeScannerActivity
import com.omegamark.remember.utility.SharedPref
import com.omegamark.remember.utility.loadImage
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
        toggle= ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        );
        drawer_layout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.setHomeAsUpIndicator(R.drawable.ic_menu)
        toggle.syncState();
        navController =Navigation.findNavController(this, R.id.fragmentNav)
        NavigationUI.setupWithNavController(navigation_view, navController)

        sharedPref=SharedPref(this)
        var user = sharedPref.get<User>("user")

        Log.d("Shiva", "user :" + user)


        navigation_view.getHeaderView(0).tvUserNameHead.text= "hello, "+user?.name
if(user?.profile_image?.length!! > 0)
{
    navigation_view.getHeaderView(0).imageView.loadImage(com.remember.api.network.Url.url + user.profile_image,this)
}

        //NavigationUI.setupActionBarWithNavController(this,navController,drawer_layout)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.home_screen, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id: Int = item.getItemId()
        if (id == R.id.action_notification) {
            Toast.makeText(applicationContext, "Action clicked", Toast.LENGTH_LONG).show()
            return true
        }
        if (id == R.id.action_scan) {
            var intent = Intent(this,QRCodeScannerActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}