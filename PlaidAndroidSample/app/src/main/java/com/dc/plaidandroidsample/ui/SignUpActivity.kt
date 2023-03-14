package com.dc.plaidandroidsample.ui

import android.widget.Toast
import com.dc.plaidandroidsample.base.BaseActivity
import com.dc.plaidandroidsample.databinding.ActivitySignUpBinding
import com.dc.plaidandroidsample.model.LoginResponse
import com.dc.plaidandroidsample.network.RetrofitClient
import com.dc.plaidandroidsample.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : BaseActivity() {
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onCreateChildView(): ChildView {
        setOnClickListener()
        return ChildView(view = binding.root)
    }

    private fun setOnClickListener() {
        binding.registerButton.setOnClickListener {
            register()
        }
    }

    private fun register() {
        val name: String = binding.name.text.toString().trim()
        val email: String = binding.email.text.toString().trim()
        val password: String = binding.password.text.toString().trim()

        setLoading(true)

        RetrofitClient.invoke().userRegister(
            name = name,
            email = email,
            password = password
        ).enqueue(object :
            Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                setLoading(false)
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        if (body.status.equals("1", false) && body.statusCode.equals(
                                "201",
                                false
                            )
                        ) {
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
                } else {
                    showToast("Something went wrong")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                setLoading(false)
                Toast.makeText(this@SignUpActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.nameLayout.disable()
            binding.emailLayout.disable()
            binding.passwordLayout.disable()
            binding.registerButton.invisible()
            binding.progressBar.show()
            binding.registerButton.disable()

        } else {
            binding.nameLayout.enable()
            binding.emailLayout.enable()
            binding.passwordLayout.enable()
            binding.registerButton.show()
            binding.progressBar.gone()
            binding.registerButton.enable()
        }
    }
}