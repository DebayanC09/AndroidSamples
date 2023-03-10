package com.dc.stripeandroidsample.ui

import com.dc.stripeandroidsample.base.BaseActivity
import com.dc.stripeandroidsample.databinding.ActivityCardOnlyViewBinding
import com.dc.stripeandroidsample.model.PaymentRequestModel
import com.dc.stripeandroidsample.model.PaymentSheetModel
import com.dc.stripeandroidsample.repository.Repository
import com.dc.stripeandroidsample.utils.*
import com.stripe.android.PaymentConfiguration
import com.stripe.android.model.ConfirmPaymentIntentParams
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult

class CardOnlyViewActivity : BaseActivity() {
    private lateinit var paymentLauncher: PaymentLauncher
    private val binding: ActivityCardOnlyViewBinding by lazy {
        ActivityCardOnlyViewBinding.inflate(layoutInflater)
    }

    override fun onCreateChildView(): ChildView {
        initializePayment()
        setOnClickListener()
        binding.cardInput.clearFocus()
        return ChildView(view = binding.root)
    }

    override fun onResume() {
        super.onResume()


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
        var amount: String = binding.cartLayout.totalPrice.text.toString().replace("₹", "").trim()
        amount = (amount.toDouble() * 100).toInt().toString()
        val currency = "INR"
        val userId: String = getUserdata()?.id ?: ""

        binding.payButton.invisible()
        binding.payButton.disable()
        binding.progressBar.show()

        Repository.initiatePayment(
            model = PaymentRequestModel(
                amount = amount,
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
                    //val customerId: String = data?.customerId ?: ""
                    //val ephemeralKey: String = data?.ephemeralKey ?: ""

                    confirmPayment(paymentIntent)
                }

                override fun error(message: String) {
                    binding.payButton.show()
                    binding.payButton.enable()
                    binding.progressBar.gone()
                    showToast(message)
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