package com.omegamark.remember.viewmodels.Post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.remember.api.repository.APIRepository

class CreatePostViewModelfactory(private var apiRepository: APIRepository)  : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       return  CreatePostViewModel(apiRepository) as T
    }
}