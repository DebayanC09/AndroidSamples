package com.dc.googlemapsample.utils

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast

fun <T> Context.openActivity(
    it: Class<T>,
) {
    val intent = Intent(this, it)
    startActivity(intent)
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun Context.showToast(message: String?) {
    message?.let {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    }
}