package com.omegamark.remember.viewmodels.members

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remember.api.models.post.ApiResponse
import com.remember.api.repository.APIRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class AddMembersFromSearchViewModel(private var apiRepository: APIRepository): ViewModel() {

     var getResponse : MutableLiveData<Response<ApiResponse>> = MutableLiveData()

     fun getAddMemeberFromSearchResponse(request:HashMap<String,String>)
    {
        try{
            viewModelScope.launch {
                var res= apiRepository.addMemberFromSearch(request)
                getResponse.postValue(res)
            }
        }catch (e:Exception){}
    }
}