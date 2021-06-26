package com.omegamark.remember.viewmodels.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.remember.api.repository.APIRepository

class ProfileUpdateViewModelFactory(private val apiRepository: APIRepository) :ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileUpdateViewModel(apiRepository) as T
    }
}