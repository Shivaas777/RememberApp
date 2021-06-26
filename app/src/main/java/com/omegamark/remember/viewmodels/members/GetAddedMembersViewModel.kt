package com.omegamark.remember.viewmodels.members

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remember.api.models.members.GetMembersResponse
import com.remember.api.models.members.GetSingleMemberResponse
import com.remember.api.repository.APIRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class GetAddedMembersViewModel(private val apiRepository: APIRepository) :ViewModel() {

    val getAddedmemberResponse :MutableLiveData<Response<GetMembersResponse>> = MutableLiveData()
    val getSingleMemberResponse :MutableLiveData<Response<GetSingleMemberResponse>> = MutableLiveData()

    lateinit var getResponse:Response<GetMembersResponse>
    lateinit var getSingleResponse:Response<GetSingleMemberResponse>
     var results: LiveData<Response<GetMembersResponse>> = MutableLiveData()
    fun getMembers(request:HashMap<String,String>,type:Int)
    {

            viewModelScope.launch {
                try{
                    if(type==1) {
                        getResponse = apiRepository.GetAddedMembers(request)
                    }
                    else if(type==2){
                        getResponse= apiRepository.searchMembers(request)
                    }

                    getAddedmemberResponse.postValue(getResponse)



                }catch (e:Exception){ e.printStackTrace() } }
            }

    fun getSingleMembers(request:HashMap<String,String>)
    {

        viewModelScope.launch {
            try{


                    getSingleResponse = apiRepository.GetMemberDetails(request)

               getSingleMemberResponse.postValue(getSingleResponse)



            }catch (e:Exception){ e.printStackTrace() } }
    }


}