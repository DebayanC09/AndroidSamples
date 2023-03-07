package com.dc.stripeandroidsample.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dc.stripeandroidsample.databinding.ActivityGooglePayViewBinding
import com.dc.stripeandroidsample.model.PaymentSheetModel
import com.dc.stripeandroidsample.repository.Repository
import com.dc.stripeandroidsample.utils.getUserdata
import com.dc.stripeandroidsample.utils.showToast
import com.stripe.android.googlepaylauncher.GooglePayEnvironment
import com.stripe.android.googlepaylauncher.GooglePayLauncher

class GooglePayViewActivity : AppCompatActivity() {
    private val binding: ActivityGooglePayViewBinding by lazy {
        ActivityGooglePayViewBinding.inflate(layoutInflater)
    }
    private lateinit var googlePayLauncher: GooglePayLauncher
    private var paymentIntent: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        googlePayLauncher = GooglePayLauncher(
            activity = this,
            config = GooglePayLauncher.Config(
                environment = GooglePayEnvironment.Test,
                merchantCountryCode = "IN",
                merchantName = "Widget Store"
            ),
            readyCallback = ::onGooglePayReady,
            resultCallback = ::onGooglePayResult
        )

        initiatePayment()

    }


    private fun initiatePayment() {
        //var amount = binding.cartLayout.totalPrice.text.toString().replace("₹", "").trim()
        // amount += "00" // appending value. (ex. 450 appended to 45000)
        val currency = "INR"
        val userId: String = getUserdata()?.id ?: ""


        Repository.initiatePayment(
            amount = "10000",
            currency = currency,
            userId = userId,
            object : Repository.Status {
                override fun success(data: PaymentSheetModel?) {

                    val paymentIntent: String = data?.paymentIntent ?: ""
                    //val customerId: String = data?.customerId ?: ""
                    //val ephemeralKey: String = data?.ephemeralKey ?: ""

                    googlePayLauncher.presentForPaymentIntent(paymentIntent)
                }

                override fun error(message: String) {

                    showToast(message)
                }
            })
    }

    private fun onGooglePayReady(isReady: Boolean) {

    }

    private fun onGooglePayResult(result: GooglePayLauncher.Result) {
        when (result) {
            GooglePayLauncher.Result.Completed -> {
                // Payment succeeded, show a receipt view
            }
            GooglePayLauncher.Result.Canceled -> {
                // User canceled the operation
            }
            is GooglePayLauncher.Result.Failed -> {
                // Operation failed; inspect `result.error` for the exception
            }
        }
    }
}