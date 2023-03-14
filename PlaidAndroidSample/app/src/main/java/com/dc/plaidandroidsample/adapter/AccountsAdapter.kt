package com.dc.plaidandroidsample.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dc.plaidandroidsample.databinding.ChildAccountsListBinding
import com.dc.plaidandroidsample.model.AccountResponse
import com.dc.plaidandroidsample.utils.showToast

class AccountsAdapter : ListAdapter<AccountResponse.AccountsModel, AccountsAdapter.ViewHolder>(DiffCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ChildAccountsListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setDataToViews(getItem(position))
        holder.onCLickListener(getItem(position))
    }

    override fun submitList(list: List<AccountResponse.AccountsModel>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    inner class ViewHolder(private val binding: ChildAccountsListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setDataToViews(item: AccountResponse.AccountsModel?) {
            item?.let {
                binding.account.text = it.account
                binding.balance.text =
                    "${it.balances?.isoCurrencyCode ?: ""} ${it.balances?.current ?: ""}"
                binding.name.text = it.name
                binding.officialName.text = it.officialName
                binding.routing.text = it.routing
                binding.wireRouting.text = it.wireRouting
                binding.type.text = it.type
                binding.subType.text = it.subtype
            }
        }

        fun onCLickListener(item: AccountResponse.AccountsModel?) {
            binding.root.setOnClickListener {
                binding.root.context.showToast(item?.accountId ?: "")
            }
        }

    }

    private object DiffCallBack : DiffUtil.ItemCallback<AccountResponse.AccountsModel>() {
        override fun areItemsTheSame(
            oldItem: AccountResponse.AccountsModel, newItem: AccountResponse.AccountsModel
        ): Boolean {
            return oldItem.accountId == newItem.accountId
        }

        override fun areContentsTheSame(
            oldItem: AccountResponse.AccountsModel, newItem: AccountResponse.AccountsModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}