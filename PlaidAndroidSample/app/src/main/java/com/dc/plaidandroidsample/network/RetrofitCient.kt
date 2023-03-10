package com.dc.plaidandroidsample.network

import com.dc.plaidandroidsample.utils.EndPoints
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    fun invoke(): ApiService {
        val logInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val okHttpClientBuilder = OkHttpClient.Builder().apply {
            addInterceptor(logInterceptor)
        }
        val retrofit = Retrofit.Builder()
            .baseUrl(EndPoints.baseUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientBuilder.build())
            .build()
        return retrofit.create(ApiService::class.java)
    }


}