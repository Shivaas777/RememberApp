package com.omegamark.remember.viewmodels.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.remember.api.repository.APIRepository

class OrderHistoryViewModelFactory(private var apiRepository: APIRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return OrderHistoryViewModel(apiRepository) as T
    }
}