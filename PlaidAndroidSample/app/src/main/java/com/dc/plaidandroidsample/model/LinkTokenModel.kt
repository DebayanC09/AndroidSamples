package com.dc.plaidandroidsample.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LinkTokenModel(
    @SerializedName("expiration")
    @Expose
    var expiration: String? = null,

    @SerializedName("link_token")
    @Expose
    var linkToken: String? = null,

    @SerializedName("request_id")
    @Expose
    var requestId: String? = null
)