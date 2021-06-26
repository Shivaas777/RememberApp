package com.omegamark.remember.viewmodels.relation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omegamark.remember.utility.hideProgreeDialog
import com.remember.api.models.relation.RelationResponse
import com.remember.api.repository.APIRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class GetRelationViewModel(private var apiRepository: APIRepository):ViewModel() {

    var relationResponse :MutableLiveData<Response<RelationResponse>> = MutableLiveData()

    fun getRelation(request : HashMap<String,String>)
    {
        viewModelScope.launch {

            try {
                var response = apiRepository.getRelation(request)
                relationResponse.value=response

            }catch (e:Exception){
                e.printStackTrace()
                hideProgreeDialog()
            }
        }
    }
}