package com.dc.plaidandroidsample.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dc.plaidandroidsample.adapter.TransactionsAdapter
import com.dc.plaidandroidsample.databinding.FragmentTransactionBinding
import com.dc.plaidandroidsample.model.TransactionListResponse
import com.dc.plaidandroidsample.network.RetrofitClient
import com.dc.plaidandroidsample.utils.getUserdata
import com.dc.plaidandroidsample.utils.gone
import com.dc.plaidandroidsample.utils.show
import com.dc.plaidandroidsample.utils.showToast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TransactionFragment : Fragment() {
    private lateinit var binding: FragmentTransactionBinding
    private val adapter: TransactionsAdapter = TransactionsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        getTransactionList()
    }

    private fun initRecyclerView() {
        binding.recyclerView.adapter = adapter
    }

    internal fun getTransactionList() {
        binding.progress.show()

        val userId: String = requireActivity().getUserdata()?.id ?: ""
        RetrofitClient.invoke().getTransactionList(userId = userId)
            .enqueue(object : Callback<TransactionListResponse> {
                override fun onResponse(
                    call: Call<TransactionListResponse>,
                    response: Response<TransactionListResponse>
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

                override fun onFailure(call: Call<TransactionListResponse>, t: Throwable) {
                    binding.progress.gone()
                    Toast.makeText(requireActivity(), t.message, Toast.LENGTH_LONG).show()
                }
            })
    }

}