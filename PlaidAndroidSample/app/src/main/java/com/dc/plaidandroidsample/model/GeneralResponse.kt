package com.dc.plaidandroidsample.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GeneralResponse(
    @SerializedName("statusCode")
    @Expose
    val statusCode: String? = null,

    @SerializedName("status")
    @Expose
    val status: String? = null,

    @SerializedName("message")
    @Expose
    val message: String? = null
)