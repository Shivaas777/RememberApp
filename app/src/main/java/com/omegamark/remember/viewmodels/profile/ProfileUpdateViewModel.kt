package com.omegamark.remember.viewmodels.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remember.api.models.registration.VerificationResponse
import com.remember.api.repository.APIRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class ProfileUpdateViewModel(private var apiRepository: APIRepository) : ViewModel() {

    var getResponse: MutableLiveData<Response<VerificationResponse>> = MutableLiveData()

    fun updateProfile(request:HashMap<String,String>){

        try {
            viewModelScope.launch {

                var res = apiRepository.updateUser(request)
                getResponse.postValue(res)
            }

        }catch (e:Exception){e.printStackTrace()}
    }
}