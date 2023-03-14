package com.dc.plaidandroidsample.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("statusCode")
    @Expose
    val statusCode: String? = null,

    @SerializedName("status")
    @Expose
    val status: String? = null,

    @SerializedName("message")
    @Expose
    val message: String? = null,

    @SerializedName("user")
    @Expose
    val user: UserModel? = null
) {
    data class UserModel(
        @SerializedName("_id")
        @Expose
        val id: String? = null,

        @SerializedName("name")
        @Expose
        val name: String? = null,

        @SerializedName("email")
        @Expose
        val email: String? = null
    )
}