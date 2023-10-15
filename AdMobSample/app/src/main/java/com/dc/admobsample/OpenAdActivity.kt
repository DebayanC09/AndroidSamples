package com.dc.admobsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dc.admobsample.databinding.ActivityOpenAdBinding
import com.google.android.gms.ads.LoadAdError

class OpenAdActivity : AppCompatActivity() {
    private val binding : ActivityOpenAdBinding by lazy {
        ActivityOpenAdBinding.inflate(layoutInflater)
    }
    private val adManager : AdMobManager by lazy {
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
        adManager.loadOpenAd(object : AdMobManager.CallBack {
            override fun success() {
                Toast.makeText(this@OpenAdActivity, "Ad Loaded", Toast.LENGTH_SHORT).show()
            }

            override fun failed(loadAdError: LoadAdError) {
                Toast.makeText(this@OpenAdActivity, loadAdError.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}