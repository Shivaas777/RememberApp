package com.fospe.remember.viewmodels.verification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fospe.remember.viewmodels.RegisterViewModel
import com.remember.api.repository.APIRepository

class VerificationViewModelFactory (private val apiRepository: APIRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        VerificationViewModel(apiRepository) as T
}