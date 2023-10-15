package com.dc.admobsample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dc.admobsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setOnClickListener()

    }

    private fun setOnClickListener() {
        binding.openAd.setOnClickListener {
            startActivity(Intent(this@MainActivity, OpenAdActivity::class.java))
        }

        binding.bannerAd.setOnClickListener {
            startActivity(Intent(this@MainActivity, BannerAdActivity::class.java))
        }
    }
}