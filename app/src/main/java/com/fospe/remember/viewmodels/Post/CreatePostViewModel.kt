package com.fospe.remember.viewmodels.Post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remember.api.models.post.ApiResponse
import com.remember.api.repository.APIRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class CreatePostViewModel(private var apiRepository: APIRepository):ViewModel() {

    var getCreatePostResponse : MutableLiveData<Response<ApiResponse>> = MutableLiveData()

    fun createPost(request: HashMap<String,String>)
    {
        viewModelScope.launch {
            try {
               var response = apiRepository.CreatePost(request)
                getCreatePostResponse.value=response
            }
            catch (e:Exception){e.printStackTrace()}
        }
    }
}