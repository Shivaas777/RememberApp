package com.remember.api.network

import com.remember.api.models.members.GetMembersResponse
import com.remember.api.models.post.ApiResponse
import com.remember.api.models.post.PostDetailsResponse
import com.remember.api.models.post.PostResponse
import com.remember.api.models.registration.RegistrationResponse
import com.remember.api.models.registration.VerificationResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RememberApi {

    @POST("/user/register")
    suspend fun registerUser(@Body body: HashMap<String, String>) : RegistrationResponse

    @POST("/user/verifyRegister")
    suspend fun verifyUser(@Body body: HashMap<String, String>) : VerificationResponse

    @POST("/user/login")
    suspend fun userLogin(@Body body: HashMap<String, String>) : VerificationResponse

    @POST("/user/getPost")
    suspend fun getPost(@Body body :HashMap<String,String>) : PostResponse

    @POST("/user/getPostDetails")
    suspend fun getPostDetials(@Body body :HashMap<String,String>) : Response<PostDetailsResponse>

    @POST("/user/getYourMember")
    suspend fun getAddedMembers(@Body body: HashMap<String, String>):Response<GetMembersResponse>

    @POST("/user/addPost")
    suspend fun createPost(@Body body: HashMap<String, String>):Response<ApiResponse>

    @POST("/user/addComment")
    suspend fun addComment(@Body body: HashMap<String, String>):Response<ApiResponse>

    @POST("/user/addComment")
    suspend fun deleteMember(@Body body: HashMap<String, String>):Response<ApiResponse>

    @POST("/user/searchMember")
    suspend fun searchMember(@Body body: HashMap<String, String>):Response<GetMembersResponse>

}