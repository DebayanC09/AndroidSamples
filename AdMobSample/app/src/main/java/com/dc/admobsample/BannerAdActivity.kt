package com.dc.admobsample

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dc.admobsample.databinding.ActivityBannerAdBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError

class BannerAdActivity : AppCompatActivity() {
    private val binding: ActivityBannerAdBinding by lazy {
        ActivityBannerAdBinding.inflate(layoutInflater)
    }
    private val adManager: AdMobManager by lazy {
        AdMobManager(this@BannerAdActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.button.setOnClickListener {
            loadAd()
        }
    }

    private fun loadAd() {
        displayViews(showProgress = true)

        adManager.loadBannerAd(binding.adViewStatic, listener = object : AdListener() {
            override fun onAdClicked() {
                Toast.makeText(this@BannerAdActivity, "Ad Clicked", Toast.LENGTH_SHORT).show()
            }

            override fun onAdClosed() {
                Toast.makeText(this@BannerAdActivity, "Ad Closed", Toast.LENGTH_SHORT).show()
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                Toast.makeText(this@BannerAdActivity, adError.message, Toast.LENGTH_SHORT).show()
                displayViews(showProgress = false)
            }

            override fun onAdImpression() {}
            override fun onAdLoaded() {
                Toast.makeText(this@BannerAdActivity, "Ad Loaded", Toast.LENGTH_SHORT).show()
                displayViews(showProgress = false)
            }

            override fun onAdOpened() {
                Toast.makeText(this@BannerAdActivity, "Ad Opened", Toast.LENGTH_SHORT).show()
            }
        })


        adManager.loadBannerAd(binding.adViewDynamic, listener = object : AdListener() {
            override fun onAdClicked() {
                Toast.makeText(this@BannerAdActivity, "Ad Clicked", Toast.LENGTH_SHORT).show()
            }

            override fun onAdClosed() {
                Toast.makeText(this@BannerAdActivity, "Ad Closed", Toast.LENGTH_SHORT).show()
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                Toast.makeText(this@BannerAdActivity, adError.message, Toast.LENGTH_SHORT).show()
                displayViews(showProgress = false)
            }

            override fun onAdImpression() {}
            override fun onAdLoaded() {
                Toast.makeText(this@BannerAdActivity, "Ad Loaded", Toast.LENGTH_SHORT).show()
                displayViews(showProgress = false)
            }

            override fun onAdOpened() {
                Toast.makeText(this@BannerAdActivity, "Ad Opened", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayViews(showProgress: Boolean) {
        if (showProgress) {
            binding.progress.visibility = View.VISIBLE
            binding.button.visibility = View.GONE
            binding.adViewStatic.visibility = View.GONE
            binding.adViewDynamic.visibility = View.GONE
        } else {
            binding.progress.visibility = View.GONE
            binding.button.visibility = View.VISIBLE
            binding.adViewStatic.visibility = View.VISIBLE
            binding.adViewDynamic.visibility = View.VISIBLE
        }
    }
}