package com.fospe.remember.datastore


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class UserPreferences(context : Context) {

    private val applicationContext =  context.applicationContext
    //private val dataStore : DataStore<Preferences>
    private val Context.dataStore by preferencesDataStore("app_preferences")



    val user: Flow<String?>
        get() = applicationContext.dataStore.data.map { preferences ->
            preferences[KEY_USER]
        }

    suspend fun saveUser(bookmark: String) {
        applicationContext.dataStore.edit { preferences ->
            preferences[KEY_USER] = bookmark
        }
    }


    companion object {
        val KEY_USER = stringPreferencesKey("key_user")
    }

}


