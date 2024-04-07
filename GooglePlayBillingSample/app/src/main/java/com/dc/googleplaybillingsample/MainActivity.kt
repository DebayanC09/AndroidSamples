package com.dc.googleplaybillingsample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.SkuDetailsParams
import com.dc.googleplaybillingsample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val list = ArrayList<QueryProductDetailsParams.Product>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        list.add(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("product_id_example")
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        )
        binding.button.setOnClickListener {
            call()
        }
    }

    private fun call() {
        val list = ArrayList<String>()
        list.add("android.test.purchased")


        val purchasesUpdatedListener =
            PurchasesUpdatedListener { billingResult, purchases ->

            }

        val billingClient = BillingClient.newBuilder(this@MainActivity)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()

        val productList: ArrayList<QueryProductDetailsParams.Product> = ArrayList()
        productList.add(
            QueryProductDetailsParams.Product.newBuilder().setProductId("android.test.purchased")
                .setProductType(BillingClient.ProductType.INAPP).build()
        )

        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    val queryProductDetailsParams =
                        QueryProductDetailsParams.newBuilder().setProductList(
                            productList
                        ).build()

                    billingClient.queryProductDetailsAsync(queryProductDetailsParams) { billingResult,
                                                                                        productDetailsList ->

                        val productDetailsParamsList : ArrayList<BillingFlowParams.ProductDetailsParams> = ArrayList()
                        for (productDetails in productDetailsList) {
                            productDetailsParamsList.add(BillingFlowParams.ProductDetailsParams.newBuilder()
                                // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                                .setProductDetails(productDetails)
                                // to get an offer token, call ProductDetails.subscriptionOfferDetails()
                                // for a list of offers that are available to the user
                                //.setOfferToken(selectedOfferToken)
                                .build())
                        }



                        val billingFlowParams = BillingFlowParams.newBuilder()
                            .setProductDetailsParamsList(productDetailsParamsList)
                            .build()

// Launch the billing flow
                        val billingResult =
                            billingClient.launchBillingFlow(this@MainActivity, billingFlowParams)

                    }
//                    // The BillingClient is ready. You can query purchases here.
//                    val params = SkuDetailsParams.newBuilder()
//                    params.setSkusList(list).setType(BillingClient.SkuType.INAPP)
//
//                    billingClient.querySkuDetailsAsync(params.build()){
//                        billingResult,skuDetailsList ->
//
//                        skuDetailsList?.forEach { item ->
//                            val flowPurchase = BillingFlowParams.newBuilder()
//                                .setSkuDetails(item)
//                                .build()
//
//                            val billingResult = billingClient.launchBillingFlow(this@MainActivity, flowPurchase)
//                        }
//                    }
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }


}