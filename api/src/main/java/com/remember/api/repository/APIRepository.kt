package com.remember.api.repository

import com.remember.api.models.members.GetMembersResponse
import com.remember.api.models.members.GetSingleMemberResponse
import com.remember.api.models.members.UploadImageResponse
import com.remember.api.models.post.ApiResponse
import com.remember.api.models.post.PostDetailsResponse
import com.remember.api.models.post.PostResponse
import com.remember.api.models.registration.RegistrationResponse
import com.remember.api.models.registration.VerificationResponse
import com.remember.api.models.relation.RelationResponse
import com.remember.api.models.store.OrderHistoryResponse
import com.remember.api.models.store.StoreListResponse
import com.remember.api.network.RememberClient
import okhttp3.RequestBody
import retrofit2.Response

class APIRepository(){

    suspend fun registerUser(requestBody: HashMap<String, String>): RegistrationResponse = RememberClient.api.registerUser(requestBody)
    suspend fun verifyUser(requestBody: HashMap<String, String>): VerificationResponse= RememberClient.api.verifyUser(requestBody)
    suspend fun LoginUser(requestBody: HashMap<String, String>): VerificationResponse= RememberClient.api.userLogin(requestBody)
    suspend fun GetPost(requestBody: HashMap<String, String>): PostResponse= RememberClient.api.getPost(requestBody)
    suspend fun GetPostDetails(requestBody: HashMap<String, String>):Response<PostDetailsResponse> =RememberClient.api.getPostDetials(requestBody)
    suspend fun GetAddedMembers(requestBody: HashMap<String, String>):Response<GetMembersResponse> = RememberClient.api.getAddedMembers((requestBody))
    suspend fun GetMemberDetails(requestBody: HashMap<String, String>):Response<GetSingleMemberResponse> = RememberClient.api.getMemberDetails((requestBody))
    suspend fun CreatePost(requestBody: HashMap<String, String>):Response<ApiResponse> = RememberClient.api.createPost((requestBody))
    suspend fun addComment(requestBody: HashMap<String, String>):Response<ApiResponse> = RememberClient.api.addComment((requestBody))
    suspend fun searchMembers(requestBody: HashMap<String, String>):Response<GetMembersResponse> = RememberClient.api.searchMember((requestBody))
    suspend fun getRelation(requestBody: HashMap<String, String>):Response<RelationResponse> = RememberClient.api.getRelation((requestBody))
    suspend fun uploadImage(requestBody: RequestBody):Response<UploadImageResponse> = RememberClient.api.uploadImage(requestBody)
    suspend fun addMember(requestBody: HashMap<String, String>):Response<ApiResponse> = RememberClient.api.addMember((requestBody))
    suspend fun deleteMember(requestBody: HashMap<String, String>):Response<ApiResponse> = RememberClient.api.deleteMember((requestBody))
    suspend fun addMemberFromSearch(requestBody: HashMap<String, String>):Response<ApiResponse> = RememberClient.api.addMemberFromSearch((requestBody))
    suspend fun getStoreList(requestBody: HashMap<String, String>):Response<StoreListResponse> = RememberClient.api.getStoreList((requestBody))
    suspend fun buyProduct(requestBody: HashMap<String, Object>):Response<ApiResponse> = RememberClient.api.buyProduct((requestBody))
    suspend fun getOrderHistory(requestBody: HashMap<String, String>):Response<OrderHistoryResponse> = RememberClient.api.getOrderHistory((requestBody))
    suspend fun updateUser(requestBody: HashMap<String, String>):Response<VerificationResponse> = RememberClient.api.updateUser((requestBody))
}
