package com.dc.plaidandroidsample.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dc.plaidandroidsample.databinding.ActivitySplashBinding
import com.dc.plaidandroidsample.utils.getUserdata
import com.dc.plaidandroidsample.utils.openActivity

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