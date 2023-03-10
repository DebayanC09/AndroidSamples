package com.dc.stripeandroidsample.base

interface ItemClickListener {
    fun onItemClick(position: Int, option: String = "")
}