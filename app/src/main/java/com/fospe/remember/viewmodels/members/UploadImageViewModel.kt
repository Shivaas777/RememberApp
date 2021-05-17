package com.fospe.remember.viewmodels.members

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remember.api.models.members.UploadImageResponse
import com.remember.api.repository.APIRepository
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Response

class UploadImageViewModel(private var apiRepository: APIRepository) :ViewModel() {

    var uploadImageResponse :MutableLiveData<Response<UploadImageResponse>> = MutableLiveData()

    fun getResponse(requestBody: RequestBody){

        viewModelScope.launch {

            try{
                var response = apiRepository.uploadImage(requestBody)
                uploadImageResponse.postValue(response)
            }catch(e:Exception){e.printStackTrace()}
        }
    }
}