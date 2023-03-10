package com.dc.stripeandroidsample.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PaymentRequestModel(
    @SerializedName("userId")
    @Expose
    var userId: String? = null,

    @SerializedName("amount")
    @Expose
    var amount: String? = null,

    @SerializedName("currency")
    @Expose
    var currency: String? = null,

    @SerializedName("merchant_country")
    @Expose
    var merchantCountry: String? = null,

    @SerializedName("payment_method_types")
    @Expose
    var payment_method_types: List<String>? = null,

    )