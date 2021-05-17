package com.remember.api.network

import com.google.gson.GsonBuilder
import com.remember.api.models.registration.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



class RememberClient {

    companion object{
        private val retrofit:Retrofit by lazy {

            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            System.out.println("Network called")
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            Retrofit.Builder()
                .baseUrl(Url.url)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val api:RememberApi by lazy {
            retrofit.create(RememberApi::class.java)
        }
    }
}
