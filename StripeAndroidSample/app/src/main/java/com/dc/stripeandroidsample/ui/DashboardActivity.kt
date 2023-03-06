package com.dc.stripeandroidsample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dc.stripeandroidsample.databinding.ActivityDashboardBinding
import com.dc.stripeandroidsample.utils.openActivity

class DashboardActivity : AppCompatActivity() {
    private val binding: ActivityDashboardBinding by lazy {
        ActivityDashboardBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.paymentSheet.setOnClickListener {
            openActivity(className = PaymentSheetViewActivity::class.java)
        }
    }
}