package com.dc.stripeandroidsample.ui

import com.dc.stripeandroidsample.base.BaseActivity
import com.dc.stripeandroidsample.databinding.ActivityDashboardBinding
import com.dc.stripeandroidsample.utils.openActivity

class DashboardActivity : BaseActivity() {
    private val binding: ActivityDashboardBinding by lazy {
        ActivityDashboardBinding.inflate(layoutInflater)
    }

    override fun onCreateChildView(): ChildView {
        setOnClickListener()

        return ChildView(view = binding.root, showBack = false)
    }

    private fun setOnClickListener() {
        binding.paymentSheet.setOnClickListener {
            openActivity(className = PaymentSheetViewActivity::class.java)
        }
        binding.cardOnly.setOnClickListener {
            openActivity(className = CardOnlyViewActivity::class.java)
        }
        binding.playground.setOnClickListener {
            openActivity(className = PlaygroundViewActivity::class.java)
        }
    }
}