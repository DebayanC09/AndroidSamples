package com.dc.mediamanagersample.remote


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://api.escuelajs.co/api/v1/files/"

    fun invokeWithOutAuth(): ApiService {
        val logInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val okHttpClientBuilder = OkHttpClient.Builder().apply {
            addInterceptor(logInterceptor)
        }
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientBuilder.build())
            .build()
        return retrofit.create(ApiService::class.java)
    }

}