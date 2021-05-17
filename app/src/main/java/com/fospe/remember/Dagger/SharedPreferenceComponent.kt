package com.fospe.remember.Dagger

import com.fospe.remember.utility.SharedPref
import dagger.Component

@Component(modules = arrayOf(SharePreferenceModule::class))
interface SharedPreferenceComponent {

    fun getSharedPref() : SharedPref
}