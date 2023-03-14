package com.dc.plaidandroidsample.network

import com.dc.plaidandroidsample.model.*
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
        @Field("user_id") userId: String,
    ): Call<LinkTokenResponse>

    @FormUrlEncoded
    @POST(EndPoints.setAccessToken)
    fun setAccessToken(
        @Field("user_id") userId: String,
        @Field("public_token") publicToken: String,
    ): Call<GeneralResponse>

    @FormUrlEncoded
    @POST(EndPoints.getAccountList)
    fun getAccountList(
        @Field("user_id") userId: String,
    ): Call<AccountResponse>

    @FormUrlEncoded
    @POST(EndPoints.getTransactionList)
    fun getTransactionList(
        @Field("user_id") userId: String,
    ): Call<TransactionListResponse>

}