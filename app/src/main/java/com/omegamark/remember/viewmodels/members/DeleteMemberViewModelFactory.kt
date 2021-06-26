package com.omegamark.remember.viewmodels.members

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.remember.api.repository.APIRepository

class DeleteMemberViewModelFactory(private var apiRepository: APIRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DeleteMemberViewModel(apiRepository) as T
    }
}