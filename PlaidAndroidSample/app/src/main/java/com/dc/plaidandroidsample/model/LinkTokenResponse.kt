package com.dc.plaidandroidsample.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LinkTokenResponse(
    @SerializedName("statusCode")
    @Expose
    val statusCode: String? = null,

    @SerializedName("status")
    @Expose
    val status: String? = null,

    @SerializedName("message")
    @Expose
    val message: String? = null,

    @SerializedName("data")
    @Expose
    val data: LinkTokenModel? = null
) {
    data class LinkTokenModel(
        @SerializedName("expiration")
        @Expose
        val expiration: String? = null,

        @SerializedName("link_token")
        @Expose
        val linkToken: String? = null,

        @SerializedName("request_id")
        @Expose
        val requestId: String? = null
    )
}