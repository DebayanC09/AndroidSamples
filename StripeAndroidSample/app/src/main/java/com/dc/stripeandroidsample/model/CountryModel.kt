package com.dc.stripeandroidsample.model

import com.stripe.android.core.model.CountryCode

data class CountryModel(
    val code: CountryCode,
    val name: String
)