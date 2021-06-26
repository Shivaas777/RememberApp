package com.omegamark.remember.viewmodels.members

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.remember.api.repository.APIRepository

class AddMemberFromSearchViewModelFactory(private var apiRepository: APIRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddMembersFromSearchViewModel(apiRepository) as T
    }
}