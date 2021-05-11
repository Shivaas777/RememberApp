package com.fospe.remember.viewmodels.members

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.remember.api.repository.APIRepository

class GetAddedMembersViewModelfactory(private val apiRepository: APIRepository) :ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GetAddedMembersViewModel(apiRepository) as T
    }
}