package com.omegamark.remember.viewmodels.store

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remember.api.models.store.StoreListResponse
import com.remember.api.repository.APIRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class StoreListViewModel(private var apiRepository: APIRepository) : ViewModel() {

     var storeListResponse: MutableLiveData<Response<StoreListResponse>> = MutableLiveData()

    fun getStoreListResponse(request : HashMap<String,String>){

        try {
            viewModelScope.launch {
                 var response = apiRepository.getStoreList(request)
                storeListResponse.postValue(response)
            }

        }catch (e:Exception){e.printStackTrace()}
    }
}