package com.dc.admobsample

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dc.admobsample.databinding.ActivityOpenAdBinding
import com.google.android.gms.ads.LoadAdError

class OpenAdActivity : AppCompatActivity() {
    private val binding: ActivityOpenAdBinding by lazy {
        ActivityOpenAdBinding.inflate(layoutInflater)
    }
    private val adManager: AdMobManager by lazy {
        AdMobManager(this@OpenAdActivity)
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
        adManager.loadOpenAd(object : AdMobManager.CallBack {
            override fun success() {
                Toast.makeText(this@OpenAdActivity, "Ad Loaded", Toast.LENGTH_SHORT).show()
                displayViews(showProgress = false)
            }

            override fun failed(loadAdError: LoadAdError) {
                Toast.makeText(this@OpenAdActivity, loadAdError.message, Toast.LENGTH_SHORT).show()
                displayViews(showProgress = false)
            }
        })
    }

    private fun displayViews(showProgress: Boolean) {
        if (showProgress) {
            binding.progress.visibility = View.VISIBLE
            binding.button.visibility = View.GONE
        } else {
            binding.progress.visibility = View.GONE
            binding.button.visibility = View.VISIBLE
        }
    }
}