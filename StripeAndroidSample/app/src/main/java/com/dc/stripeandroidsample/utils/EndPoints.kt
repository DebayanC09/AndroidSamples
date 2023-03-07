package com.dc.stripeandroidsample.utils

object EndPoints {
    private const val isLive = true

    fun baseUrl(): String {
        return if (isLive) {
            "https://lazy-lime-coypu-boot.cyclic.app/"
        } else {
            "http://192.168.0.13:5000/"
        }
    }

    const val userLogin = "stripe/login"
    const val paymentSheet = "stripe/payment-sheet"
    const val userRegister = "stripe/register"
}