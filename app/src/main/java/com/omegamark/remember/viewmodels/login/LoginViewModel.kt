package com.omegamark.remember.viewmodels.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remember.api.models.registration.VerificationResponse
import com.remember.api.repository.APIRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val apiRepository: APIRepository) :ViewModel() {

    val loginresponse : MutableLiveData<VerificationResponse> = MutableLiveData()

    fun getLoginResponse(requestBody: HashMap<String, String>)
    {
        try {


            viewModelScope.launch {

                val res = apiRepository.LoginUser(requestBody)
                loginresponse.value = res
            }
        }catch (e:Exception) {e.printStackTrace()}
    }
}