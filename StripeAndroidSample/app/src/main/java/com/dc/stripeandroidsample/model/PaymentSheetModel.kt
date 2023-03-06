package com.dc.stripeandroidsample.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PaymentSheetModel(
    @SerializedName("paymentIntent")
    @Expose
    var paymentIntent: String? = null,

    @SerializedName("ephemeralKey")
    @Expose
    var ephemeralKey: String? = null,

    @SerializedName("customerId")
    @Expose
    var customerId: String? = null
)