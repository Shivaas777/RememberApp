package com.omegamark.remember.viewmodels.verification

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remember.api.models.registration.VerificationResponse
import com.remember.api.repository.APIRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class VerificationViewModel(private val apiRepository: APIRepository)  : ViewModel(){

    var verifyResponse :MutableLiveData<VerificationResponse> = MutableLiveData()

    fun getVerificationResponse(requestBody: HashMap<String, String>)
    {
        try {


            viewModelScope.launch {


                val response = apiRepository.verifyUser(requestBody)
                Log.d("shiva", "response" +response)
                verifyResponse.value= response
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}