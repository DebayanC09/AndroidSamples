package com.dc.stripeandroidsample.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PaymentSheetResponse(
    @SerializedName("statusCode")
    @Expose
    var statusCode: String? = null,

    @SerializedName("status")
    @Expose
    var status: String? = null,

    @SerializedName("message")
    @Expose
    var message: String? = null,

    @SerializedName("data")
    @Expose
    var data: PaymentSheetModel? = null
)