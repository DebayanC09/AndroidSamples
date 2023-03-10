package com.dc.stripeandroidsample.repository

import com.dc.stripeandroidsample.model.PaymentRequestModel
import com.dc.stripeandroidsample.model.PaymentSheetModel
import com.dc.stripeandroidsample.model.PaymentSheetResponse
import com.dc.stripeandroidsample.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object Repository {


    fun initiatePayment(model: PaymentRequestModel, status: Status) {

        RetrofitClient.invoke()
            .requestPaymentSheet(model)
            .enqueue(object :
                Callback<PaymentSheetResponse> {
                override fun onResponse(
                    call: Call<PaymentSheetResponse>,
                    response: Response<PaymentSheetResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            if (body.status.equals("1", false) && body.statusCode.equals(
                                    "200",
                                    false
                                )
                            ) {

                                status.success(body.data)
                            } else {
                                status.error(body.message ?: "Something went wrong")
                            }
                        } ?: kotlin.run {
                            status.error("Something went wrong")
                        }
                    } else {
                        status.error(response.body()?.message ?: "Something went wrong")
                    }
                }

                override fun onFailure(call: Call<PaymentSheetResponse>, t: Throwable) {
                    status.error(t.message ?: "Something went wrong")
                }
            })
    }

    interface Status {
        fun success(data: PaymentSheetModel?)
        fun error(message: String)
    }
}