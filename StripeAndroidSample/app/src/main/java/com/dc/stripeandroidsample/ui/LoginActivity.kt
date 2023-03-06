package com.dc.stripeandroidsample.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dc.stripeandroidsample.databinding.ActivityLoginBinding
import com.dc.stripeandroidsample.model.LoginResponse
import com.dc.stripeandroidsample.network.RetrofitClient
import com.dc.stripeandroidsample.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.loginButton.setOnClickListener {
            login()
        }

        binding.signUpButton.setOnClickListener {
            openActivity(SignUpActivity::class.java)
        }
    }

    private fun login() {
        val email : String = binding.email.text.toString().trim()
        val password : String = binding.password.text.toString().trim()

        setLoading(true)

        RetrofitClient.invoke().userLogin(email = email, password = password).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                setLoading(false)
                if(response.isSuccessful){
                    response.body()?.let { body ->
                        if (body.status.equals("1", false) && body.statusCode.equals("200", false)) {
                            body.user?.let {
                                setUserdata(it)
                                openActivity(DashboardActivity::class.java, clearTask = true)

                            }
                            body.message?.let {
                                showToast(it)
                            } ?: kotlin.run {
                                showToast("")
                            }
                        } else {
                            body.message?.let {
                                showToast(it)
                            } ?: kotlin.run {
                                showToast("Something went wrong")
                            }
                        }
                    } ?: kotlin.run {
                        showToast("Something went wrong")
                    }
                }else{
                    showToast("Something went wrong")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                setLoading(false)
                Toast.makeText(this@LoginActivity,t.message,Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.emailLayout.disable()
            binding.passwordLayout.disable()
            binding.loginButton.invisible()
            binding.progressBar.show()
            binding.loginButton.disable()

        } else {
            binding.emailLayout.enable()
            binding.passwordLayout.enable()
            binding.loginButton.show()
            binding.progressBar.gone()
            binding.loginButton.enable()
        }
    }
}