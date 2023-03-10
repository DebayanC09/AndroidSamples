package com.dc.stripeandroidsample.network

import com.dc.stripeandroidsample.model.LoginResponse
import com.dc.stripeandroidsample.model.PaymentRequestModel
import com.dc.stripeandroidsample.model.PaymentSheetResponse
import com.dc.stripeandroidsample.utils.EndPoints
import retrofit2.Call
import retrofit2.http.Body
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
        @Field("address") address: String,
        @Field("city") city: String,
        @Field("state") state: String,
        @Field("country") country: String,
        @Field("postalCode") postalCode: String,
    ): Call<LoginResponse>


    @POST(EndPoints.paymentSheet)
    fun requestPaymentSheet(
        @Body model: PaymentRequestModel,
    ): Call<PaymentSheetResponse>

}