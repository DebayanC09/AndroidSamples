package com.dc.stripeandroidsample.ui


import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.dc.stripeandroidsample.base.BaseActivity
import com.dc.stripeandroidsample.databinding.ActivityPlaygroundViewBinding
import com.dc.stripeandroidsample.model.PaymentRequestModel
import com.dc.stripeandroidsample.model.PaymentSheetModel
import com.dc.stripeandroidsample.repository.Repository
import com.dc.stripeandroidsample.utils.*
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult

class PlaygroundViewActivity : BaseActivity() {
    private val binding: ActivityPlaygroundViewBinding by lazy {
        ActivityPlaygroundViewBinding.inflate(layoutInflater)
    }
    private lateinit var paymentSheet: PaymentSheet
    private var currency: String = ""
    private var merchantCountry: String = ""

    override fun onCreateChildView(): ChildView {
        paymentSheet = PaymentSheet(this, ::onPaymentSheetResult)
        initSpinners()
        setOnClickListener()

        return ChildView(view = binding.root)
    }

    private fun setOnClickListener() {
        binding.payButton.setOnClickListener {
            initiatePayment()
        }
    }

    private fun initiatePayment() {

        val userId: String = getUserdata()?.id ?: ""

        binding.payButton.invisible()
        binding.payButton.disable()
        binding.progressBar.show()


        Repository.initiatePayment(
            model = PaymentRequestModel(
                amount = "12044",
                currency = currency,
                userId = userId,
                merchantCountry = merchantCountry,
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

    private fun initSpinners() {
        binding.merchantCountrySpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            MERCHANT_COUNTRY_LIST
        )
        binding.merchantCountrySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    merchantCountry = MERCHANT_COUNTRY_LIST[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

        binding.currencySpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            CURRENCY_LIST
        )
        binding.currencySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    currency = CURRENCY_LIST[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
    }
}