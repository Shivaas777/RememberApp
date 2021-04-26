package com.remember.api.repository

import com.remember.api.models.post.PostResponse
import com.remember.api.models.registration.RegistrationResponse
import com.remember.api.models.registration.VerificationResponse
import com.remember.api.network.RememberClient

class APIRepository() {

    suspend fun registerUser(requestBody: HashMap<String, String>): RegistrationResponse = RememberClient.api.registerUser(requestBody)
    suspend fun verifyUser(requestBody: HashMap<String, String>): VerificationResponse= RememberClient.api.verifyUser(requestBody)
    suspend fun LoginUser(requestBody: HashMap<String, String>): VerificationResponse= RememberClient.api.userLogin(requestBody)
    suspend fun GetPost(requestBody: HashMap<String, String>): PostResponse= RememberClient.api.getPost(requestBody)
}