package com.omegamark.remember.viewmodels.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.remember.api.repository.APIRepository

class LoginViewModelFactory(private val apiRepository: APIRepository) :ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
       LoginViewModel(apiRepository) as T
}