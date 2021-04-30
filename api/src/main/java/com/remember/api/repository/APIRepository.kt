package com.remember.api.repository

import com.remember.api.models.post.PostDetailsResponse
import com.remember.api.models.post.PostResponse
import com.remember.api.models.registration.RegistrationResponse
import com.remember.api.models.registration.VerificationResponse
import com.remember.api.network.RememberClient
import retrofit2.Response

class APIRepository() {

    suspend fun registerUser(requestBody: HashMap<String, String>): RegistrationResponse = RememberClient.api.registerUser(requestBody)
    suspend fun verifyUser(requestBody: HashMap<String, String>): VerificationResponse= RememberClient.api.verifyUser(requestBody)
    suspend fun LoginUser(requestBody: HashMap<String, String>): VerificationResponse= RememberClient.api.userLogin(requestBody)
    suspend fun GetPost(requestBody: HashMap<String, String>): PostResponse= RememberClient.api.getPost(requestBody)
    suspend fun GetPostDetails(requestBody: HashMap<String, String>):Response<PostDetailsResponse> =RememberClient.api.getPostDetials(requestBody)
}