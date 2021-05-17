package com.fospe.remember.utility

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import javax.inject.Inject
import javax.inject.Named


class SharedPref @Inject constructor(private var context : Context){


    private val sharedPrefFile = "kotlinsharedpreference"
    val sharedPreferences: SharedPreferences = context.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)



    fun <T> put(`object`: T, key: String) {
        //Convert object to JSON String.
        val jsonString = GsonBuilder().create().toJson(`object`)
        //Save that String in SharedPreferences
        sharedPreferences.edit().putString(key, jsonString).apply()
    }

    fun  putString(value :String?, key: String) {

        sharedPreferences.edit().putString(key, value).apply()
    }

    /**
     * Used to retrieve object from the Preferences.
     *
     * @param key Shared Preference key with which object was saved.
     **/
    inline fun <reified T> get(key: String): T? {
        //We read JSON String which was saved.
        val value = sharedPreferences.getString(key, null)
        //JSON String was found which means object can be read.
        //We convert this JSON String to model object. Parameter "c" (of
        //type Class < T >" is used to cast.
        return GsonBuilder().create().fromJson(value, T::class.java)
    }

    fun getString(key: String) :String
    {
         var value:String = sharedPreferences.getString(key, null)  !!
        return value
    }

}