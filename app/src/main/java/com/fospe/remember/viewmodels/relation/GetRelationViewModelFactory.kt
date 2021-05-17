package com.fospe.remember.viewmodels.relation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.remember.api.repository.APIRepository

class GetRelationViewModelFactory(private var apiRepository: APIRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GetRelationViewModel(apiRepository) as T
    }
}