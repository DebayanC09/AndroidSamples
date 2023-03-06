package com.dc.stripeandroidsample.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dc.stripeandroidsample.databinding.ActivitySplashBinding
import com.dc.stripeandroidsample.utils.getUserdata
import com.dc.stripeandroidsample.utils.openActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (getUserdata() != null) {
            openActivity(className = DashboardActivity::class.java, clearTask = true)
        } else {
            openActivity(className = LoginActivity::class.java, clearTask = true)

        }
    }


}