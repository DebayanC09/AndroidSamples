package com.dc.mlkitsampe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dc.mlkitsampe.databinding.ActivityMainBinding
import com.dc.mlkitsampe.facedetection.FaceDetectionActivity

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
        binding.faceDetection.setOnClickListener {
            startActivity(Intent(this@MainActivity, FaceDetectionActivity::class.java))
        }
    }
}