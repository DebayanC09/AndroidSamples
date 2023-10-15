package com.dc.admobsample

import android.app.Application
import com.google.android.gms.ads.MobileAds

class App: Application() {
    override fun onCreate() {
        MobileAds.initialize(this@App){
            super.onCreate()
        }
    }
}