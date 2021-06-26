package com.omegamark.remember.viewmodels.Post

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omegamark.remember.utility.hideProgreeDialog
import com.remember.api.models.post.PostDetailsResponse
import com.remember.api.repository.APIRepository
import kotlinx.coroutines.launch

class GetPostDetailsViewModel(private val apiRepository: APIRepository) :ViewModel() {

    var postDetailsResponse:MutableLiveData<PostDetailsResponse> = MutableLiveData()

    fun getPostDetailsResponse(request :HashMap<String,String>)
    {
        viewModelScope.launch {
            try {
                 var response = apiRepository.GetPostDetails(request)

                if(response.isSuccessful)
                {
                    postDetailsResponse.value=response.body()
                }
                else{
                    postDetailsResponse.value=null;
                }
            }
            catch (e: Exception){
                hideProgreeDialog()
                e.printStackTrace()
            }
        }
    }
}