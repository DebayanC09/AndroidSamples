package com.dc.plaidandroidsample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dc.plaidandroidsample.adapter.AccountsAdapter
import com.dc.plaidandroidsample.databinding.FragmentAccountBinding
import com.dc.plaidandroidsample.model.AccountResponse
import com.dc.plaidandroidsample.network.RetrofitClient
import com.dc.plaidandroidsample.utils.getUserdata
import com.dc.plaidandroidsample.utils.gone
import com.dc.plaidandroidsample.utils.show
import com.dc.plaidandroidsample.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private val adapter: AccountsAdapter = AccountsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        getAccountDetails()
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = adapter
    }

    internal fun getAccountDetails() {
        binding.progress.show()

        val userId: String = requireActivity().getUserdata()?.id ?: ""
        RetrofitClient.invoke().getAccountList(userId = userId)
            .enqueue(object : Callback<AccountResponse> {
                override fun onResponse(
                    call: Call<AccountResponse>,
                    response: Response<AccountResponse>
                ) {
                    binding.progress.gone()
                    if (response.isSuccessful) {
                        response.body()?.let { body ->
                            if (body.status.equals("1", false) && body.statusCode.equals(
                                    "200",
                                    false
                                )
                            ) {
                                body.data?.let { data ->
                                    adapter.submitList(data)

                                }
                            } else {
                                body.message?.let {
                                    requireActivity().showToast(it)
                                } ?: kotlin.run {
                                    requireActivity().showToast("Something went wrong")
                                }
                            }
                        } ?: kotlin.run {
                            requireActivity().showToast("Something went wrong")
                        }
                    } else {
                        requireActivity().showToast("Something went wrong")
                    }
                }

                override fun onFailure(call: Call<AccountResponse>, t: Throwable) {
                    binding.progress.gone()
                    Toast.makeText(requireActivity(), t.message, Toast.LENGTH_LONG).show()
                }
            })
    }

}