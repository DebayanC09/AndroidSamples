package com.dc.stripeandroidsample.network

import com.dc.stripeandroidsample.model.LoginResponse
import com.dc.stripeandroidsample.model.PaymentSheetResponse
import com.dc.stripeandroidsample.utils.EndPoints
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
        @Field("address") address: String,
        @Field("city") city: String,
        @Field("state") state: String,
        @Field("country") country: String,
        @Field("postalCode") postalCode: String,
    ): Call<LoginResponse>


    @FormUrlEncoded
    @POST(EndPoints.paymentSheet)
    fun requestPaymentSheet(
        @Field("userId") userId: String,
        @Field("amount") amount: String,
        @Field("currency") currency: String,
    ): Call<PaymentSheetResponse>


//
//
//    @POST(EndPoints.userRegister)
//    suspend fun userRegister(@Body request: RegisterRequestModel): LoginResponse
////
////    @GET(EndPoints.refreshToken)
////    fun refreshToken(@Header("Authorization") token: String?): Call<TokenResponse>
////
////    @GET(EndPoints.todoList)
////    fun todoList(): Call<TodoListResponse>
////
////    @FormUrlEncoded
////    @POST(EndPoints.addTodo)
////    fun addTodo(
////        @Field("title") title: String?,
////        @Field("description") description: String?,
////        @Field("dateTime") dateTime: String?,
////        @Field("priority") priority: String?,
////    ): Call<TodoResponse>
////
////    @FormUrlEncoded
////    @POST(EndPoints.updateTodo)
////    fun updateTodo(
////        @Field("todoId") todoId: String?,
////        @Field("title") title: String?,
////        @Field("description") description: String?,
////        @Field("dateTime") dateTime: String?,
////        @Field("priority") priority: String?,
////    ): Call<TodoResponse>
////
////    @FormUrlEncoded
////    @POST(EndPoints.deleteTodo)
////    fun deleteTodo(@Field("todoId") todoId: String): Call<TodoResponse>
}