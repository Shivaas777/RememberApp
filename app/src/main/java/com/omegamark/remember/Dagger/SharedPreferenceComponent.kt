package com.omegamark.remember.Dagger

import com.omegamark.remember.utility.SharedPref
import dagger.Component

@Component(modules = arrayOf(SharePreferenceModule::class))
interface SharedPreferenceComponent {

    fun getSharedPref() : SharedPref
}