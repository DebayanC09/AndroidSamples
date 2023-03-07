package com.dc.stripeandroidsample.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dc.stripeandroidsample.databinding.ActivityGooglePayViewBinding

class GooglePayViewActivity : AppCompatActivity() {
    private val binding :  ActivityGooglePayViewBinding by lazy {
        ActivityGooglePayViewBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}