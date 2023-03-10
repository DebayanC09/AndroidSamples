package com.dc.plaidandroidsample.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dc.plaidandroidsample.databinding.ActivityDashboardBinding
import com.dc.plaidandroidsample.model.LinkTokenResponse
import com.dc.plaidandroidsample.network.RetrofitClient
import com.dc.plaidandroidsample.utils.getUserdata
import com.dc.plaidandroidsample.utils.showToast
import com.plaid.link.OpenPlaidLink
import com.plaid.link.configuration.LinkTokenConfiguration
import com.plaid.link.result.LinkExit
import com.plaid.link.result.LinkSuccess
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {
    private val binding: ActivityDashboardBinding by lazy {
        ActivityDashboardBinding.inflate(layoutInflater)
    }
    private val linkAccountToPlaid = registerForActivityResult(OpenPlaidLink()) { result ->
        when (result) {
            is LinkSuccess -> showSuccess(result)
            is LinkExit -> showFailure(result)
        }
    }

    private fun showSuccess(success: LinkSuccess) {
        //tokenResult.text = getString(R.string.public_token_result, success.publicToken)
        //result.text = getString(R.string.content_success)
        print(success)
    }

    private fun showFailure(exit: LinkExit) {
        showToast(exit.error?.displayMessage)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val userId: String = getUserdata()?.id ?: ""
            RetrofitClient.invoke().createLinkToken(userId = userId).enqueue(object :
                Callback<LinkTokenResponse> {
                override fun onResponse(
                    call: Call<LinkTokenResponse>,
                    response: Response<LinkTokenResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            if (body.status.equals("1", false) && body.statusCode.equals(
                                    "200",
                                    false
                                )
                            ) {
                                body.data?.let { data ->

                                    data.linkToken?.let {
                                        val tokenConfiguration = LinkTokenConfiguration.Builder().token(it).build()
                                        linkAccountToPlaid.launch(tokenConfiguration)
                                    }

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

                override fun onFailure(call: Call<LinkTokenResponse>, t: Throwable) {
                    Toast.makeText(this@DashboardActivity, t.message, Toast.LENGTH_LONG).show()
                }

            })
        }
    }
}