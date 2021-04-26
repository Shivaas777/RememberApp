package com.fospe.remember.viewmodels.Post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remember.api.models.post.PostResponse
import com.remember.api.repository.APIRepository
import kotlinx.coroutines.launch

class GetPostViewModel(private var apiRepository: APIRepository) :ViewModel() {

    var postResponse :MutableLiveData<PostResponse> = MutableLiveData()

    fun getPostResponse(requestBody : HashMap<String,String>)
    {
       try {
           viewModelScope.launch {

               var response = apiRepository.GetPost(requestBody)
               postResponse.value=response
           }

       }catch (e:Exception){
           e.printStackTrace()
       }
    }
}