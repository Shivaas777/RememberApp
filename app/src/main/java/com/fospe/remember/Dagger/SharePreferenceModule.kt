package com.fospe.remember.Dagger

import android.content.Context
import com.fospe.remember.utility.SharedPref
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
 class SharePreferenceModule(private var context: Context) {


    @Provides
    fun provideSharedPreference() = SharedPref(context)
}