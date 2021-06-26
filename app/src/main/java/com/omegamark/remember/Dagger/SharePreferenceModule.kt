package com.omegamark.remember.Dagger

import android.content.Context
import com.omegamark.remember.utility.SharedPref
import dagger.Module
import dagger.Provides

@Module
 class SharePreferenceModule(private var context: Context) {


    @Provides
    fun provideSharedPreference() = SharedPref(context)
}