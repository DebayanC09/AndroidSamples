package com.dc.plaidandroidsample.utils

object EndPoints {
    private const val isLive = false

    fun baseUrl(): String {
        return if (isLive) {
            "https://lazy-lime-coypu-boot.cyclic.app/"
        } else {
            "http://192.168.0.13:5000/"
        }
    }

    const val userLogin = "plaid/login"
    const val createLinkToken = "plaid/create-link-token"
    const val userRegister = "plaid/register"
}