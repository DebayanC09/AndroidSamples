package com.dc.stripeandroidsample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dc.stripeandroidsample.databinding.ActivityPaymentSheetViewBinding
import com.dc.stripeandroidsample.model.PaymentSheetResponse
import com.dc.stripeandroidsample.network.RetrofitClient
import com.dc.stripeandroidsample.utils.*
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentSheetViewActivity : AppCompatActivity() {
    private val binding: ActivityPaymentSheetViewBinding by lazy {
        ActivityPaymentSheetViewBinding.inflate(layoutInflater)
    }
    private lateinit var paymentSheet: PaymentSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.payButton.setOnClickListener {
            initiatePayment()
        }
    }

    private fun initiatePayment() {
        var amount = binding.cardLayout.totalPrice.text.toString().replace("₹","").trim()
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
                                val customerId: String = body.data?.customerId ?: ""
                                val ephemeralKey: String = body.data?.ephemeralKey ?: ""

                                presentPaymentSheet(paymentIntent, customerId, ephemeralKey)
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

    private fun presentPaymentSheet(
        paymentIntent: String,
        customerId: String,
        ephemeralKey: String
    ) {
        val customerConfig = PaymentSheet.CustomerConfiguration(
            customerId,
            ephemeralKey
        )
        paymentSheet.presentWithPaymentIntent(
            paymentIntent,
            PaymentSheet.Configuration(
                merchantDisplayName = "DC Merchant",
                customer = customerConfig,
                allowsDelayedPaymentMethods = true
            )
        )
    }

    private fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        when (paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                binding.payButton.enable()
                showToast("Payment Canceled")
            }
            is PaymentSheetResult.Failed -> {
                binding.payButton.enable()
                showToast("Payment Failed")
            }
            is PaymentSheetResult.Completed -> {
                binding.payButton.enable()
                showToast("Payment Completed")
            }
        }
    }
}