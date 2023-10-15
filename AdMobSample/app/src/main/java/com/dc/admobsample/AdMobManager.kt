package com.dc.admobsample

import android.app.Activity
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd

class AdMobManager(private val activity: Activity) {

    fun loadOpenAd(callBack: CallBack) {

        val request = AdRequest.Builder().build()
        AppOpenAd.load(activity, SecretKeys.OPEN_AD_UNIT_ID, request, object : AppOpenAd.AppOpenAdLoadCallback() {
            override fun onAdLoaded(ad: AppOpenAd) {
                super.onAdLoaded(ad)
                ad.show(activity)
                callBack.success()
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                callBack.failed(loadAdError)
            }
        })
    }

    interface CallBack{
        fun success()
        fun failed(loadAdError: LoadAdError)
    }
}