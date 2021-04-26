package com.remember.api.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RememberClient {

    companion object{
        private val retrofit:Retrofit by lazy {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

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
