package com.dc.plaidandroidsample.network

import com.dc.plaidandroidsample.model.LinkTokenResponse
import com.dc.plaidandroidsample.model.LoginResponse
import com.dc.plaidandroidsample.utils.EndPoints
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @FormUrlEncoded
    @POST(EndPoints.userLogin)
    fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST(EndPoints.userRegister)
    fun userRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST(EndPoints.createLinkToken)
    fun createLinkToken(
        @Field("userId") userId: String,
    ): Call<LinkTokenResponse>

}