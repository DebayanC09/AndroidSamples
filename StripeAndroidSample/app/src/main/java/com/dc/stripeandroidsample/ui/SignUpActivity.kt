package com.dc.stripeandroidsample.ui

import android.widget.Toast
import com.dc.stripeandroidsample.base.BaseActivity
import com.dc.stripeandroidsample.databinding.ActivitySignUpBinding
import com.dc.stripeandroidsample.model.LoginResponse
import com.dc.stripeandroidsample.network.RetrofitClient
import com.dc.stripeandroidsample.utils.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpActivity : BaseActivity() {
    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    override fun onCreateChildView(): ChildView {
        return ChildView(view = binding.root)
    }

    override fun onResume() {
        super.onResume()
        setOnClickListener()
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
        val address: String = binding.address.text.toString().trim()
        val city: String = binding.city.text.toString().trim()
        val state: String = binding.state.text.toString().trim()
        val country: String = binding.country.text.toString().trim()
        val postalCode: String = binding.postalCode.text.toString().trim()

        setLoading(true)

        RetrofitClient.invoke().userRegister(
            name = name,
            email = email,
            password = password,
            address = address,
            city = city,
            state = state,
            country = country,
            postalCode = postalCode
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
            binding.addressLayout.disable()
            binding.cityLayout.disable()
            binding.stateLayout.disable()
            binding.countryLayout.disable()
            binding.postalCodeLayout.disable()
            binding.registerButton.invisible()
            binding.progressBar.show()
            binding.registerButton.disable()

        } else {
            binding.nameLayout.enable()
            binding.emailLayout.enable()
            binding.passwordLayout.enable()
            binding.addressLayout.enable()
            binding.cityLayout.enable()
            binding.stateLayout.enable()
            binding.countryLayout.enable()
            binding.postalCodeLayout.enable()
            binding.registerButton.show()
            binding.progressBar.gone()
            binding.registerButton.enable()
        }
    }
}