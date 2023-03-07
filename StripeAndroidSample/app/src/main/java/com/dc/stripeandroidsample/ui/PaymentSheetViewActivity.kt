package com.dc.stripeandroidsample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dc.stripeandroidsample.databinding.ActivityPaymentSheetViewBinding
import com.dc.stripeandroidsample.model.PaymentSheetModel
import com.dc.stripeandroidsample.repository.Repository
import com.dc.stripeandroidsample.utils.*
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult

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
        var amount = binding.cartLayout.totalPrice.text.toString().replace("₹", "").trim()
        amount += "00" // appending value. (ex. 450 appended to 4500)
        val currency = "INR"
        val userId: String = getUserdata()?.id ?: ""

        binding.payButton.invisible()
        binding.payButton.disable()
        binding.progressBar.show()


        Repository.initiatePayment(
            amount = amount,
            currency = currency,
            userId = userId,
            object : Repository.Status {
                override fun success(data: PaymentSheetModel?) {
                    binding.payButton.show()
                    binding.progressBar.gone()

                    val paymentIntent: String = data?.paymentIntent ?: ""
                    val customerId: String = data?.customerId ?: ""
                    val ephemeralKey: String = data?.ephemeralKey ?: ""

                    presentPaymentSheet(paymentIntent, customerId, ephemeralKey)
                }

                override fun error(message: String) {
                    binding.payButton.show()
                    binding.payButton.enable()
                    binding.progressBar.gone()
                    showToast(message)
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