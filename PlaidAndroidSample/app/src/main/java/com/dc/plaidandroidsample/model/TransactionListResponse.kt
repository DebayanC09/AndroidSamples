package com.dc.plaidandroidsample.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TransactionListResponse(
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
    val data: List<TransactionModel>? = null
) {
    data class TransactionModel(
        @SerializedName("account_id")
        @Expose
        val accountId: String? = null,

        @SerializedName("account_owner")
        @Expose
        val accountOwner: String? = null,

        @SerializedName("amount")
        @Expose
        val amount: String? = null,

        @SerializedName("authorized_date")
        @Expose
        val authorizedDate: String? = null,

        @SerializedName("authorized_datetime")
        @Expose
        val authorizedDatetime: String? = null,

        @SerializedName("category")
        @Expose
        val category: List<String>? = null,

        @SerializedName("category_id")
        @Expose
        val categoryId: String? = null,

        @SerializedName("check_number")
        @Expose
        val checkNumber: String? = null,

        @SerializedName("date")
        @Expose
        val date: String? = null,

        @SerializedName("datetime")
        @Expose
        val datetime: String? = null,

        @SerializedName("iso_currency_code")
        @Expose
        val isoCurrencyCode: String? = null,

        @SerializedName("location")
        @Expose
        val location: Location? = null,

        @SerializedName("merchant_name")
        @Expose
        val merchantName: String? = null,

        @SerializedName("name")
        @Expose
        val name: String? = null,

        @SerializedName("payment_channel")
        @Expose
        val paymentChannel: String? = null,

        @SerializedName("payment_meta")
        @Expose
        val paymentMeta: PaymentMeta? = null,

        @SerializedName("pending")
        @Expose
        val pending: String? = null,

        @SerializedName("pending_transaction_id")
        @Expose
        val pendingTransactionId: String? = null,

        @SerializedName("personal_finance_category")
        @Expose
        val personalFinanceCategory: String? = null,

        @SerializedName("transaction_code")
        @Expose
        val transactionCode: String? = null,

        @SerializedName("transaction_id")
        @Expose
        val transactionId: String? = null,

        @SerializedName("transaction_type")
        @Expose
        val transactionType: String? = null,

        @SerializedName("unofficial_currency_code")
        @Expose
        val unofficialCurrencyCode: String? = null,
    ) {
        data class Location(
            @SerializedName("address")
            @Expose
            val address: String? = null,

            @SerializedName("city")
            @Expose
            val city: String? = null,

            @SerializedName("country")
            @Expose
            val country: String? = null,

            @SerializedName("lat")
            @Expose
            val lat: String? = null,

            @SerializedName("lon")
            @Expose
            val lon: String? = null,

            @SerializedName("postal_code")
            @Expose
            val postalCode: String? = null,

            @SerializedName("region")
            @Expose
            val region: String? = null,

            @SerializedName("store_number")
            @Expose
            val storeNumber: String? = null,
        )

        data class PaymentMeta(
            @SerializedName("by_order_of")
            @Expose
            val byOrderOf: String? = null,

            @SerializedName("payee")
            @Expose
            val payee: String? = null,

            @SerializedName("payer")
            @Expose
            val payer: String? = null,

            @SerializedName("payment_method")
            @Expose
            val paymentMethod: String? = null,

            @SerializedName("payment_processor")
            @Expose
            val paymentProcessor: String? = null,

            @SerializedName("ppd_id")
            @Expose
            val ppdId: String? = null,

            @SerializedName("reason")
            @Expose
            val reason: String? = null,

            @SerializedName("reference_number")
            @Expose
            val referenceNumber: String? = null
        )
    }
}