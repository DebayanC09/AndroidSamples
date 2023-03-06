package com.dc.stripeandroidsample

import android.app.Application
import com.dc.stripeandroidsample.utils.Credentials
import com.stripe.android.PaymentConfiguration

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        PaymentConfiguration.init(applicationContext, Credentials.stripePublicKey)
    }
}