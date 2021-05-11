package com.fospe.remember.viewmodels.Post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.remember.api.repository.APIRepository

class AddCommentViewModelFactory(private var apiRepository: APIRepository) :ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AddCommentViewModel(apiRepository) as T
    }
}