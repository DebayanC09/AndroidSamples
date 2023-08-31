package com.dc.googlemapsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dc.googlemapsample.databinding.ActivityMainBinding
import com.dc.googlemapsample.ui.view.CurrentLocationActivity
import com.dc.googlemapsample.ui.view.CurrentLocationUpdateActivity
import com.dc.googlemapsample.utils.openActivity

class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.currentLocationButton.setOnClickListener {
            openActivity(CurrentLocationActivity::class.java)
        }

        binding.currentLocationUpdateButton.setOnClickListener {
            openActivity(CurrentLocationUpdateActivity::class.java)
        }
    }
}