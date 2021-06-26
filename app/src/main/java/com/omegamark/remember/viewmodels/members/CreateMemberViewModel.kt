package com.omegamark.remember.viewmodels.members

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remember.api.models.post.ApiResponse
import com.remember.api.repository.APIRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class CreateMemberViewModel(private var apiRepository: APIRepository ):ViewModel() {

    var getResponse :MutableLiveData<Response<ApiResponse>> = MutableLiveData()

    fun getCreateMemberResponse(requestBody :HashMap<String,String>)
    {
        viewModelScope.launch {

            try{
                var response = apiRepository.addMember(requestBody)
                getResponse.postValue(response)
            }catch (e:Exception){e.printStackTrace()}
        }
    }


}