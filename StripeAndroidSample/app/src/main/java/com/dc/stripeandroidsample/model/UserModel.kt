package com.dc.stripeandroidsample.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

data class UserModel(
    @SerializedName("_id")
    @Expose
    var id: String? = null,

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("email")
    @Expose
    var email: String? = null,

    @SerializedName("customerId")
    @Expose
    var customerId: String? = null
)