package com.fospe.remember.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.remember.api.repository.APIRepository


class APIViewModelFactory(private val apiRepository: APIRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        RegisterViewModel(apiRepository) as T
}