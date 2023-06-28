package com.dc.facebooksample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dc.facebooksample.databinding.ActivityLoginBinding
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import java.util.Arrays

class LoginActivity : AppCompatActivity() {
    private val binding : ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private val callBackManager : CallbackManager by lazy {
        CallbackManager.Factory.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        registerCallBack()
    }

    private fun registerCallBack() {
        binding.facebookLogin.apply {
            registerCallback(callBackManager, object : FacebookCallback<LoginResult>{
                override fun onCancel() {
                    Toast.makeText(this@LoginActivity,"Cancel",Toast.LENGTH_SHORT).show()
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(this@LoginActivity,"Error",Toast.LENGTH_SHORT).show()
                }

                override fun onSuccess(result: LoginResult) {
                    Toast.makeText(this@LoginActivity,"Success",Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}