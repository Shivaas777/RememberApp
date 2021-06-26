package com.omegamark.remember.viewmodels.store

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remember.api.models.post.ApiResponse
import com.remember.api.repository.APIRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class BuyProductViewModel(private var apiRepository: APIRepository) : ViewModel() {

    var getBuyProductResponse :MutableLiveData<Response<ApiResponse>> = MutableLiveData()

    fun getResponse(request : HashMap<String,Object>){

        try{
            viewModelScope.launch {
                var response = apiRepository.buyProduct(request)
                getBuyProductResponse.postValue(response)
            }

        }catch (e:Exception){e.printStackTrace()}
    }
}