package com.omegamark.remember.ui.members

import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.omegamark.remember.R
import com.omegamark.remember.utility.changeToolbarText
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.gson.Gson
import kotlinx.android.synthetic.main.custom_toolbar.*
import java.io.IOException
import java.util.*


class MapActivity : AppCompatActivity() {
    var placesClient: PlacesClient? = null
    private lateinit var   mGeocoder : Geocoder
    private  var type:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        setSupportActionBar(toolbar)
        changeToolbarText("Address", supportActionBar, toolbar)
            type =intent.getIntExtra("type",0)
        Log.d("shiva",type.toString())
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyDXltr6B935rR5scIvznODXmHc5Xl00h50");
            mGeocoder= Geocoder(this,Locale.getDefault())
        }
        placesClient = Places.createClient(this);
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.place_autocomplete_fragment) as AutocompleteSupportFragment?

        autocompleteFragment?.setPlaceFields(
            Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.ADDRESS,
                Place.Field.ADDRESS_COMPONENTS,
                Place.Field.LAT_LNG
            )
        )
       autocompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
           override fun onPlaceSelected(place: Place) {

               // TODO: Get info about the selected place.
               Toast.makeText(applicationContext, place.getName(), Toast.LENGTH_SHORT).show()

               Log.d("Places", "object" + Gson().toJson(place))
             var intent = Intent();
               intent.putExtra("address",place.address)
               intent.putExtra("type",type)
               intent.putExtra("latitude",place.latLng?.latitude.toString())
               intent.putExtra("longitude",place.latLng?.longitude.toString())
               setResult(RESULT_OK,intent)
               finish()
           }

           override fun onError(place: Status) {

               Log.d("Places", "object" + Gson().toJson(place))

           }
       })
    }

    @Throws(IOException::class)
    private fun getPlaceInfo(lat: Double, lon: Double) {
        mGeocoder= Geocoder(this,Locale.getDefault())
        val addresses: List<Address> = mGeocoder.getFromLocation(lat, lon, 1)
        if (addresses[0].getPostalCode() != null) {
            val ZIP: String = addresses[0].getPostalCode()
            Log.d("Place","ZIP CODE"+ZIP)
        }
        if (addresses[0].getLocality() != null) {
            val city: String = addresses[0].getLocality()
            Log.d("Place","CITY" +city)
        }
        if (addresses[0].getAdminArea() != null) {
            val state: String = addresses[0].getAdminArea()
            Log.d("Place","STATE" +state)
        }
        if (addresses[0].getCountryName() != null) {
            val country: String = addresses[0].getCountryName()
            Log.d("Place","COUNTRY" +country)
        }
    }
}