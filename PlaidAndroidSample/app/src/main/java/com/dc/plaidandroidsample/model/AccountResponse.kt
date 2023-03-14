package com.dc.plaidandroidsample.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


data class AccountResponse(
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
    val data: List<AccountsModel>? = null
) {
    data class AccountsModel(
        @SerializedName("account_id")
        @Expose
        val accountId: String? = null,

        @SerializedName("account")
        @Expose
        val account: String? = null,

        @SerializedName("routing")
        @Expose
        val routing: String? = null,

        @SerializedName("wire_routing")
        @Expose
        val wireRouting: String? = null,

        @SerializedName("balances")
        @Expose
        val balances: AccountsBalance? = null,

        @SerializedName("mask")
        @Expose
        val mask: String? = null,

        @SerializedName("name")
        @Expose
        val name: String? = null,

        @SerializedName("official_name")
        @Expose
        val officialName: String? = null,

        @SerializedName("subtype")
        @Expose
        val subtype: String? = null,

        @SerializedName("type")
        @Expose
        val type: String? = null
    ) {
        data class AccountsBalance(
            @SerializedName("available")
            @Expose
            val available: String? = null,

            @SerializedName("current")
            @Expose
            val current: String? = null,

            @SerializedName("iso_currency_code")
            @Expose
            val isoCurrencyCode: String? = null,

            @SerializedName("limit")
            @Expose
            val limit: String? = null,

            @SerializedName("unofficial_currency_code")
            @Expose
            val unofficialCurrencyCode: String? = null
        )
    }
}