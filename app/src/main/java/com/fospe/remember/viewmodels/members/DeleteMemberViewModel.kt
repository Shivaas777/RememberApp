package com.fospe.remember.viewmodels.members

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remember.api.models.post.ApiResponse
import com.remember.api.repository.APIRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class DeleteMemberViewModel(private var apiRepository: APIRepository):ViewModel() {

    var getDeleteResponse :MutableLiveData<Response<ApiResponse>> = MutableLiveData()

    fun getResponse(request :HashMap<String,String>)
    {
        viewModelScope.launch {

            try{
                var response = apiRepository.deleteMember(request)
                getDeleteResponse.postValue(response)
            }catch(e:Exception) {}
        }
    }
}