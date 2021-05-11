package com.fospe.remember.viewmodels.members

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remember.api.models.members.GetMembersResponse
import com.remember.api.repository.APIRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class GetAddedMembersViewModel(private val apiRepository: APIRepository) :ViewModel() {

    val getAddedmemberResponse :MutableLiveData<Response<GetMembersResponse>> = MutableLiveData()
    lateinit var getResponse:Response<GetMembersResponse>
     var results: LiveData<Response<GetMembersResponse>> = MutableLiveData()
    fun getMembers(request:HashMap<String,String>,type:Int)
    {

            viewModelScope.launch {
                try{
                    if(type==1) {
                        getResponse = apiRepository.GetAddedMembers(request)
                    }
                    else{
                        getResponse= apiRepository.searchMembers(request)
                    }
                    getAddedmemberResponse.value=getResponse



                }catch (e:Exception){ e.printStackTrace() } }
            }


}