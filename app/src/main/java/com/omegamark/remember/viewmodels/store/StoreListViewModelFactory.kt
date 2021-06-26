package com.omegamark.remember.viewmodels.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.remember.api.repository.APIRepository

class StoreListViewModelFactory(private var apiRepository: APIRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StoreListViewModel(apiRepository) as T
    }
}