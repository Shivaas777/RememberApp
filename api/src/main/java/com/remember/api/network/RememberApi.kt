package com.remember.api.network

import com.remember.api.models.post.PostResponse
import com.remember.api.models.registration.RegistrationResponse
import com.remember.api.models.registration.Verification
import com.remember.api.models.registration.VerificationResponse
import retrofit2.http.Body
import retrofit2.http.FieldMap
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

}