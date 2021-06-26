package com.omegamark.remember.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import androidx.lifecycle.viewModelScope
import com.remember.api.models.registration.RegistrationResponse
import com.remember.api.repository.APIRepository

import kotlinx.coroutines.launch



class RegisterViewModel (private val apiRepository: APIRepository) : ViewModel() {

    val myResponse: MutableLiveData<RegistrationResponse> = MutableLiveData()

    fun registerUser(requestBody: HashMap<String, String>){

        try {

              viewModelScope.launch {
                val response = apiRepository.registerUser(requestBody)
                myResponse.value = response
            }
        }catch (e: Exception){}
    }

}