package com.dc.ironsourceadsample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dc.ironsourceadsample.databinding.ActivityMainBinding

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
        binding.interstitial.setOnClickListener {
            startActivity(Intent(this@MainActivity,InterstitialActivity::class.java))
        }
    }


}