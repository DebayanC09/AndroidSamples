package com.dc.plaidandroidsample.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dc.plaidandroidsample.databinding.ChildTransactionsListBinding
import com.dc.plaidandroidsample.model.TransactionListResponse
import com.dc.plaidandroidsample.utils.showToast

class TransactionsAdapter :
    ListAdapter<TransactionListResponse.TransactionModel, TransactionsAdapter.ViewHolder>(
        DiffCallBack
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ChildTransactionsListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setDataToViews(getItem(position))
        holder.onCLickListener(getItem(position))
    }

    override fun submitList(list: List<TransactionListResponse.TransactionModel>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    inner class ViewHolder(private val binding: ChildTransactionsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setDataToViews(item: TransactionListResponse.TransactionModel?) {
            item?.let {
                binding.name.text = it.name ?: ""
                binding.amount.text = "${it.isoCurrencyCode} ${it.amount}"
                binding.date.text = it.date
            }
        }

        fun onCLickListener(item: TransactionListResponse.TransactionModel?) {
            binding.root.setOnClickListener {
                binding.root.context.showToast(item?.accountId ?: "")
            }
        }

    }

    private object DiffCallBack :
        DiffUtil.ItemCallback<TransactionListResponse.TransactionModel>() {
        override fun areItemsTheSame(
            oldItem: TransactionListResponse.TransactionModel,
            newItem: TransactionListResponse.TransactionModel
        ): Boolean {
            return oldItem.accountId == newItem.accountId
        }

        override fun areContentsTheSame(
            oldItem: TransactionListResponse.TransactionModel,
            newItem: TransactionListResponse.TransactionModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}