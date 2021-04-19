package com.remember.api

import retrofit2.Retrofit

class RememberClient {

    val retrofit = Retrofit.Builder().baseUrl("").build()
}
