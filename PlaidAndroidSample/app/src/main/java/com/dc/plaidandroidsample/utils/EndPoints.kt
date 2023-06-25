package com.dc.plaidandroidsample.utils

object EndPoints {
    private const val isLive = true

    fun baseUrl(): String {
        return if (isLive) {
            "https://cyan-super-piglet.cyclic.app"
        } else {
            "http://192.168.100.2:5000/"
        }
    }

    const val userLogin = "plaid/login"
    const val userRegister = "plaid/register"
    const val createLinkToken = "plaid/create-link-token"
    const val setAccessToken = "plaid/set-access-token"
    const val getAccountList = "plaid/get-account-list"
    const val getTransactionList = "plaid/get-transaction-list"
}