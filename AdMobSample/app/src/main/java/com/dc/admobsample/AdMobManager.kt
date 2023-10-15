package com.dc.admobsample

import android.app.Activity
import android.widget.FrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd

class AdMobManager(private val activity: Activity) {

    private val request = AdRequest.Builder().build()

    fun loadOpenAd(callBack: CallBack) {
        AppOpenAd.load(
            activity,
            SecretKeys.OPEN_AD_UNIT_ID,
            request,
            object : AppOpenAd.AppOpenAdLoadCallback() {
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

    fun loadBannerAd(adView: AdView, listener: AdListener = object : AdListener() {}) {
        adView.loadAd(request)
        adView.adListener = listener
    }

    fun loadBannerAd(view: FrameLayout, listener: AdListener = object : AdListener() {}) {
        val adView = AdView(activity)
        view.addView(adView)
        adView.adUnitId = SecretKeys.BANNER_AD_UNIT_ID
        adView.setAdSize(AdSize.BANNER)
        adView.loadAd(request)
        adView.adListener = listener
    }

    interface CallBack {
        fun success()
        fun failed(loadAdError: LoadAdError)
    }
}