package com.dc.ironsourceadsample

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dc.ironsourceadsample.databinding.ActivityBannerBinding
import com.ironsource.mediationsdk.ISBannerSize
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.LevelPlayBannerListener


class BannerActivity : AppCompatActivity() {
    private val binding: ActivityBannerBinding by lazy {
        ActivityBannerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        IronSource.init(this, SecretKeys.APP_KEY)
        initIronSource()
    }

    private fun initIronSource() {
        displayViews(showProgress = true)
        val banner = IronSource.createBanner(this, ISBannerSize.BANNER)

        binding.adLayout.addView(
            banner, 0, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )
        IronSource.loadBanner(banner)

        banner.levelPlayBannerListener = object : LevelPlayBannerListener {
            override fun onAdLoaded(adInfo: AdInfo?) {
                displayViews(showProgress = false)
                Toast.makeText(this@BannerActivity, "Ad Loaded", Toast.LENGTH_SHORT).show()
            }
            override fun onAdLoadFailed(error: IronSourceError?) {
                displayViews(showProgress = false)
                Toast.makeText(this@BannerActivity, "Ad loading failed", Toast.LENGTH_SHORT).show()
            }
            override fun onAdClicked(adInfo: AdInfo?) {}
            override fun onAdLeftApplication(adInfo: AdInfo?) {}
            override fun onAdScreenPresented(adInfo: AdInfo?) {}
            override fun onAdScreenDismissed(adInfo: AdInfo?) {}
        }
    }

    private fun displayViews(showProgress : Boolean) {
        if(showProgress){
            binding.progress.visibility = View.VISIBLE
            binding.adLayout.visibility = View.GONE
        }else{
            binding.progress.visibility = View.GONE
            binding.adLayout.visibility = View.VISIBLE
        }
    }
}