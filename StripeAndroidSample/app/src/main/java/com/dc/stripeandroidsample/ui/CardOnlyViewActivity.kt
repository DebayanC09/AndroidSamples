package com.dc.stripeandroidsample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dc.stripeandroidsample.databinding.ActivityCardOnlyViewBinding
import com.dc.stripeandroidsample.model.PaymentSheetResponse
import com.dc.stripeandroidsample.network.RetrofitClient
import com.dc.stripeandroidsample.utils.*
import com.stripe.android.PaymentConfiguration
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CardOnlyViewActivity : AppCompatActivity() {
    private lateinit var paymentLauncher: PaymentLauncher
    private val binding: ActivityCardOnlyViewBinding by lazy {
        ActivityCardOnlyViewBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initializePayment()

        setOnClickListener()

        binding.cardInput.clearFocus()
    }

    private fun initializePayment() {
        val paymentConfiguration = PaymentConfiguration.getInstance(applicationContext)
        paymentLauncher = PaymentLauncher.create(
            this@CardOnlyViewActivity,
            paymentConfiguration.publishableKey,
            paymentConfiguration.stripeAccountId,
            ::onPaymentResult
        )
    }

    private fun setOnClickListener() {
        binding.payButton.setOnClickListener {
            initiatePayment()
        }
    }

    private fun initiatePayment() {
        var amount = binding.cartLayout.totalPrice.text.toString().replace("₹", "").trim()
        amount += "00" // appending value. (ex. 450 appended to 4500)
        val currency = "INR"
        val userId: String = getUserdata()?.id ?: ""

        binding.payButton.invisible()
        binding.payButton.disable()
        binding.progressBar.show()

        RetrofitClient.invoke()
            .requestPaymentSheet(userId = userId, amount = amount, currency = currency)
            .enqueue(object :
                Callback<PaymentSheetResponse> {
                override fun onResponse(
                    call: Call<PaymentSheetResponse>,
                    response: Response<PaymentSheetResponse>
                ) {
                    binding.payButton.show()
                    binding.progressBar.gone()

                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            if (body.status.equals("1", false) && body.statusCode.equals(
                                    "200",
                                    false
                                )
                            ) {

                                val paymentIntent: String = body.data?.paymentIntent ?: ""
                                //val customerId: String = body.data?.customerId ?: ""
                                //val ephemeralKey: String = body.data?.ephemeralKey ?: ""

                                confirmPayment(paymentIntent)
                            } else {
                                binding.payButton.enable()
                                body.message?.let {
                                    showToast(it)
                                } ?: kotlin.run {
                                    showToast("Something went wrong")
                                }
                            }
                        } ?: kotlin.run {
                            binding.payButton.enable()
                            showToast("Something went wrong")
                        }
                    } else {
                        binding.payButton.enable()
                        showToast("Something went wrong")
                    }
                }

                override fun onFailure(call: Call<PaymentSheetResponse>, t: Throwable) {
                    binding.payButton.show()
                    binding.payButton.enable()
                    binding.progressBar.gone()
                    showToast(t.message)
                }
            })
    }

    private fun confirmPayment(paymentIntent: String) {
        if (::paymentLauncher.isInitialized) {
            binding.cardInput.paymentMethodCreateParams?.let { params ->
                val confirmParams = ConfirmPaymentIntentParams.createWithPaymentMethodCreateParams(
                    params,
                    paymentIntent
                )
                paymentLauncher.confirm(confirmParams)
            }
        }
    }

    private fun onPaymentResult(paymentResult: PaymentResult) {
        when (paymentResult) {
            is PaymentResult.Canceled -> {
                binding.payButton.enable()
                showToast("Payment Canceled")
            }
            is PaymentResult.Failed -> {
                binding.payButton.enable()
                showToast("Payment Failed")
            }
            is PaymentResult.Completed -> {
                binding.payButton.enable()
                binding.cardInput.clear()
                binding.cardInput.clearFocus()
                showToast("Payment Completed")
            }
        }
    }
}