package com.omegamark.remember.viewmodels.Post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remember.api.models.post.ApiResponse
import com.remember.api.repository.APIRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class AddCommentViewModel(private var apiRepository: APIRepository):ViewModel() {

    var getCommentResponse : MutableLiveData<Response<ApiResponse>> = MutableLiveData()

    fun addComment(request: HashMap<String,String>)
    {
        viewModelScope.launch {
            try{
                var response: Response<ApiResponse> = apiRepository.addComment(request)
                getCommentResponse.value=response

            }catch (e:Exception){
                e.printStackTrace()
            }
        }
    }
}