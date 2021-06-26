package com.omegamark.remember.viewmodels.Post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.remember.api.repository.APIRepository

class GetPostDetailsViewModelFactory(private var apiRepository: APIRepository) :ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GetPostDetailsViewModel(apiRepository)as T
    }
}