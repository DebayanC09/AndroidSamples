package com.dc.ironsourceadsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.dc.ironsourceadsample.databinding.ActivityInterstitialBinding
import com.ironsource.mediationsdk.IronSource
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo
import com.ironsource.mediationsdk.logger.IronSourceError
import com.ironsource.mediationsdk.sdk.LevelPlayInterstitialListener

class InterstitialActivity : AppCompatActivity() {
    private val binding : ActivityInterstitialBinding by lazy {
        ActivityInterstitialBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initIronSource()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.button.setOnClickListener {
            showAd()
        }
    }

    private fun showAd() {
        if (IronSource.isInterstitialReady()) {
            IronSource.showInterstitial()
        } else {
            Toast.makeText(this, "Not Ready", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initIronSource() {
        displayViews(showProgress = true)

        IronSource.init(this, SecretKeys.APP_KEY)

        IronSource.setLevelPlayInterstitialListener(object : LevelPlayInterstitialListener {
            override fun onAdReady(adInfo: AdInfo) {
                displayViews(showProgress = false)
            }
            override fun onAdLoadFailed(error: IronSourceError) {
                displayViews(showProgress = false)
            }
            override fun onAdOpened(adInfo: AdInfo) {}
            override fun onAdClosed(adInfo: AdInfo) {}
            override fun onAdShowFailed(error: IronSourceError, adInfo: AdInfo) {}
            override fun onAdClicked(adInfo: AdInfo) {}
            override fun onAdShowSucceeded(adInfo: AdInfo) {}
        })

        IronSource.loadInterstitial()
    }

    private fun displayViews(showProgress : Boolean) {
        if(showProgress){
            binding.progress.visibility = View.VISIBLE
            binding.button.visibility = View.GONE
        }else{
            binding.progress.visibility = View.GONE
            binding.button.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        IronSource.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        IronSource.onPause(this)
    }
}