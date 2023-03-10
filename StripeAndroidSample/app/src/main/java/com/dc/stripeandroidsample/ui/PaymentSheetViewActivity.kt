package com.dc.stripeandroidsample.ui

import com.dc.stripeandroidsample.base.BaseActivity
import com.dc.stripeandroidsample.databinding.ActivityPaymentSheetViewBinding
import com.dc.stripeandroidsample.model.PaymentRequestModel
import com.dc.stripeandroidsample.model.PaymentSheetModel
import com.dc.stripeandroidsample.repository.Repository
import com.dc.stripeandroidsample.utils.*
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult

class PaymentSheetViewActivity : BaseActivity() {
    private val binding: ActivityPaymentSheetViewBinding by lazy {
        ActivityPaymentSheetViewBinding.inflate(layoutInflater)
    }
    private lateinit var paymentSheet: PaymentSheet

    override fun onCreateChildView(): ChildView {
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        setOnClickListener()

        return ChildView(view = binding.root)
    }

    private fun setOnClickListener() {
        binding.payButton.setOnClickListener {
            initiatePayment()
        }
    }

    private fun initiatePayment() {
        var amount: String = binding.cartLayout.totalPrice.text.toString().replace("₹", "").trim()
        amount = (amount.toDouble() * 100).toInt().toString()
        val currency = "INR"
        val userId: String = getUserdata()?.id ?: ""

        binding.payButton.invisible()
        binding.payButton.disable()
        binding.progressBar.show()


        Repository.initiatePayment(
            model = PaymentRequestModel(
                amount = amount.toInt().toString(),
                currency = currency,
                userId = userId,
                merchantCountry = MerchantCountry.INDIA.name,
                payment_method_types = emptyList()
            ),
            status = object : Repository.Status {
                override fun success(data: PaymentSheetModel?) {
                    binding.payButton.show()
                    binding.progressBar.gone()

                    val paymentIntent: String = data?.paymentIntent ?: ""
                    val customerId: String = data?.customerId ?: ""
                    val ephemeralKey: String = data?.ephemeralKey ?: ""
                    val publishableKey: String = data?.publishableKey ?: ""

                    presentPaymentSheet(paymentIntent, customerId, ephemeralKey, publishableKey)
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
        ephemeralKey: String,
        publishableKey: String
    ) {
        PaymentConfiguration.init(applicationContext, publishableKey)
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