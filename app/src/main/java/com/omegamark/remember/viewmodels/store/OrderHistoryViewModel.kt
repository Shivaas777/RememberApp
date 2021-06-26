package com.omegamark.remember.viewmodels.store

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remember.api.models.store.OrderHistoryResponse
import com.remember.api.repository.APIRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class OrderHistoryViewModel(private var apiRepository: APIRepository): ViewModel() {

    var getOrderHistoryResponse : MutableLiveData<Response<OrderHistoryResponse>> = MutableLiveData()

    fun orderHistoryResponse(request :HashMap<String,String>)
    {
        try{
            viewModelScope.launch {
                val res = apiRepository.getOrderHistory(request)
                getOrderHistoryResponse.postValue(res)
            }
        }catch (e:Exception){e.printStackTrace()}
    }
}