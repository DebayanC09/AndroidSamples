package com.dc.facebooksample

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dc.facebooksample.databinding.ActivityMainBinding
import java.security.MessageDigest
import java.util.Base64

class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //keyHash()

        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.facebookLogin.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
        binding.userPost.setOnClickListener {
            startActivity(Intent(this,UserFeedActivity::class.java))
        }
    }

    private fun keyHash() {
        try {
            val info : PackageInfo = packageManager.getPackageInfo(packageName,PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val messageDigest = MessageDigest.getInstance("SHA")
                messageDigest.update(signature.toByteArray())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                   val keyHashes  = String(Base64.getEncoder().encode(messageDigest.digest()))
                    Log.d("KEY_HASHES", keyHashes)
                }
            }
        } catch (e: Exception) {
        }
    }
}