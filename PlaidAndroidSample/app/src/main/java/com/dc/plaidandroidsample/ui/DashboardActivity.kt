package com.dc.plaidandroidsample.ui

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dc.plaidandroidsample.adapter.TabAdapter
import com.dc.plaidandroidsample.base.BaseActivity
import com.dc.plaidandroidsample.databinding.ActivityDashboardBinding
import com.dc.plaidandroidsample.model.GeneralResponse
import com.dc.plaidandroidsample.model.LinkTokenResponse
import com.dc.plaidandroidsample.network.RetrofitClient
import com.dc.plaidandroidsample.utils.*
import com.google.android.material.tabs.TabLayoutMediator
import com.plaid.link.OpenPlaidLink
import com.plaid.link.configuration.LinkTokenConfiguration
import com.plaid.link.result.LinkExit
import com.plaid.link.result.LinkSuccess
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : BaseActivity() {
    private val binding: ActivityDashboardBinding by lazy {
        ActivityDashboardBinding.inflate(layoutInflater)
    }
    private val adapter : TabAdapter by lazy {
        TabAdapter(this@DashboardActivity)
    }

    private val linkAccountToPlaid = registerForActivityResult(OpenPlaidLink()) { result ->
        when (result) {
            is LinkSuccess -> showSuccess(result)
            is LinkExit -> showFailure(result)
        }
    }

    private fun showSuccess(success: LinkSuccess) {
        binding.progress.show()
        binding.addButton.invisible()
        binding.addButton.disable()

        val userId: String = getUserdata()?.id ?: ""
        val publicToken = success.publicToken
        RetrofitClient.invoke().setAccessToken(userId = userId, publicToken = publicToken)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    binding.progress.gone()
                    binding.addButton.show()
                    binding.addButton.enable()
                    showToast(response.body()?.message)
                    val pos: Int = binding.viewPager.currentItem
                    val activeFragment: Fragment = adapter.createFragment(pos)
                    binding.viewPager.currentItem = binding.viewPager.currentItem
                    if (pos == 0) {
                        (activeFragment as AccountFragment).getAccountDetails()
                    }else if (pos == 1) {
                        (activeFragment as TransactionFragment).getTransactionList()
                    }
                }

                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    binding.progress.gone()
                    binding.addButton.show()
                    binding.addButton.enable()
                    showToast(t.message)
                }

            })

    }

    private fun showFailure(exit: LinkExit) {
        showToast(exit.error?.displayMessage)
    }

    override fun onCreateChildView(): ChildView {
        initViewPager()
        setOnClickListener()
        return ChildView(view = binding.root, title = "Dashboard", showBack = false)
    }

    private fun initViewPager() {
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Accounts"
                }
                1 -> {
                    tab.text = "Transactions"
                }
                else -> {
                    tab.text = "Accounts"
                }
            }
        }.attach()
    }

    private fun setOnClickListener() {
        binding.addButton.setOnClickListener {
            addAccount()
        }
    }

    private fun addAccount() {
        binding.progress.show()
        binding.addButton.invisible()
        binding.addButton.disable()

        val userId: String = getUserdata()?.id ?: ""
        RetrofitClient.invoke().createLinkToken(userId = userId).enqueue(object :
            Callback<LinkTokenResponse> {
            override fun onResponse(
                call: Call<LinkTokenResponse>,
                response: Response<LinkTokenResponse>
            ) {
                binding.progress.gone()
                binding.addButton.show()
                binding.addButton.enable()
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        if (body.status.equals("1", false) && body.statusCode.equals(
                                "200",
                                false
                            )
                        ) {
                            body.data?.let { data ->

                                data.linkToken?.let {
                                    val tokenConfiguration =
                                        LinkTokenConfiguration.Builder().token(it).build()
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
                binding.progress.gone()
                binding.addButton.show()
                binding.addButton.enable()
                Toast.makeText(this@DashboardActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }
}