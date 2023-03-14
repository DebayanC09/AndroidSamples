package com.dc.plaidandroidsample.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dc.plaidandroidsample.ui.AccountFragment
import com.dc.plaidandroidsample.ui.TransactionFragment

class TabAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val accountFragment = AccountFragment()
    private val transactionFragment = TransactionFragment()

    override fun getItemCount(): Int = 2



    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                accountFragment
            }
            1 -> {
                transactionFragment
            }
            else -> {
                accountFragment
            }
        }
    }
}