package com.dc.plaidandroidsample.utils

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.dc.plaidandroidsample.model.LoginResponse
import com.google.gson.Gson

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.enable() {
    this.isEnabled = true
}

fun View.disable() {
    this.isEnabled = false
}

fun Context.showToast(message: String?) {
    if (message != null) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

fun Context.getUserdata(): LoginResponse.UserModel? {
    val sharedPreferences: SharedPreferences =
        getSharedPreferences(USER_CREDENTIAL, Context.MODE_PRIVATE)
    return Gson().fromJson(sharedPreferences.getString("userData", null), LoginResponse.UserModel::class.java)
}

fun Context.setUserdata(userModel: LoginResponse.UserModel?) {
    val sharedPreferences: SharedPreferences = getSharedPreferences(
        USER_CREDENTIAL, Context.MODE_PRIVATE
    )
    val editor: SharedPreferences.Editor = sharedPreferences.edit()
    val data: String = Gson().toJson(userModel)
    editor.putString("userData", data)
    editor.apply()
}

fun <T> Context.openActivity(
    className: Class<T>,
    clearTask: Boolean = false,
    bundleKey: String = "",
    bundle: Bundle? = null
) {
    val intent = Intent(this, className)
    if (clearTask) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    } else {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    }
    intent.putExtra(bundleKey, bundle)
    startActivity(intent)
}
