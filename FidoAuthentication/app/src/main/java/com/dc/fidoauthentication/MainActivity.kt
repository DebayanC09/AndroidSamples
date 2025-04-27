package com.dc.fidoauthentication

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dc.fidoauthentication.databinding.ActivityMainBinding
import com.google.android.gms.fido.Fido
import com.google.android.gms.fido.fido2.Fido2ApiClient
import com.google.android.gms.fido.fido2.api.common.*
import com.google.android.gms.tasks.Task


class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_REGISTER = 1
    private val REQUEST_CODE_SIGN = 2


    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val helper: Helper by lazy {
        Helper()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.registerFido2Button.setOnClickListener { registerFido2() }
        binding.signFido2Button.setOnClickListener { signFido2() }

    }


    private fun registerFido2() {
        val options = helper.generateRegisterOption()

        val fido2ApiClient: Fido2ApiClient = Fido.getFido2ApiClient(applicationContext)
        val fido2PendingIntentTask = fido2ApiClient.getRegisterIntent(options)
        fido2PendingIntentTask.addOnSuccessListener { fido2PendingIntent ->
            if (fido2PendingIntent.hasPendingIntent()) {
                try {
                    fido2PendingIntent.launchPendingIntent(
                        this@MainActivity,
                        REQUEST_CODE_REGISTER
                    )
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun handleSignResponse(fido2Response: ByteArray?) {
        if (fido2Response != null) {
            val response = AuthenticatorAssertionResponse.deserializeFromBytes(fido2Response)
            val keyHandleBase64 = Base64.encodeToString(response.keyHandle, Base64.DEFAULT)
            val clientDataJson = String(response.clientDataJSON, Charsets.UTF_8)
            val authenticatorDataBase64 = Base64.encodeToString(response.authenticatorData, Base64.DEFAULT)
            val signatureBase64 = Base64.encodeToString(response.signature, Base64.DEFAULT)


            val signFido2Result = "Authenticator Login Response\n\n" +
                    "keyHandleBase64:\n" +
                    "$keyHandleBase64\n\n" +
                    "clientDataJSON:\n" +
                    "$clientDataJson\n\n" +
                    "authenticatorDataBase64:\n" +
                    "$authenticatorDataBase64\n\n" +
                    "signatureBase64:\n" +
                    "$signatureBase64\n"

            binding.resultText.text = signFido2Result
        }
    }

    private fun handleRegisterResponse(fido2Response: ByteArray?) {
        if (fido2Response != null) {
            val response = AuthenticatorAttestationResponse.deserializeFromBytes(fido2Response)
            val keyHandleBase64 = Base64.encodeToString(response.keyHandle, Base64.DEFAULT)
            val clientDataJson = String(response.clientDataJSON, Charsets.UTF_8)
            val attestationObjectBase64 =
                Base64.encodeToString(response.attestationObject, Base64.DEFAULT)

            helper.storeKeyHandle(response.keyHandle, this@MainActivity)
            binding.signFido2Button.isEnabled = true


            val registerFido2Result = "Authenticator Register Response\n\n" +
                    "keyHandleBase64:\n" +
                    "$keyHandleBase64\n\n" +
                    "clientDataJSON:\n" +
                    "$clientDataJson\n\n" +
                    "attestationObjectBase64:\n" +
                    "$attestationObjectBase64\n"

            binding.resultText.text = registerFido2Result
        }
    }

    private fun signFido2() {

        val options = helper.generateLoginOption(this@MainActivity)
        val fido2ApiClient = Fido.getFido2ApiClient(applicationContext)
        val fido2PendingIntentTask = fido2ApiClient.getSignIntent(options)
        fido2PendingIntentTask.addOnSuccessListener { fido2PendingIntent ->
            if (fido2PendingIntent.hasPendingIntent()) {
                try {
                    fido2PendingIntent.launchPendingIntent(
                        this@MainActivity,
                        REQUEST_CODE_SIGN
                    )
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun handleErrorResponse(errorBytes: ByteArray?) {
        if (errorBytes != null) {
            val authenticatorErrorResponse =
                AuthenticatorErrorResponse.deserializeFromBytes(errorBytes)
            val errorName = authenticatorErrorResponse.errorCode.name
            val errorMessage = authenticatorErrorResponse.errorMessage


            val registerFidoResult =
                "An Error Ocurred\n\nError Name:\n$errorName\n\nError Message:\n$errorMessage"
            binding.resultText.text = registerFidoResult
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        when (resultCode) {
            RESULT_OK -> {
                data?.let {
                    if (it.hasExtra(Fido.FIDO2_KEY_ERROR_EXTRA)) {
                        handleErrorResponse(data.getByteArrayExtra(Fido.FIDO2_KEY_ERROR_EXTRA))
                        //print("")
                    } else if (it.hasExtra(Fido.FIDO2_KEY_RESPONSE_EXTRA)) {
                        val fido2Response: ByteArray? =
                            data.getByteArrayExtra(Fido.FIDO2_KEY_RESPONSE_EXTRA)
                        when (requestCode) {
                            REQUEST_CODE_REGISTER -> handleRegisterResponse(fido2Response)
                            REQUEST_CODE_SIGN -> handleSignResponse(fido2Response)
                        }
                    }
                }
            }

            RESULT_CANCELED -> {

            }

            else -> {

            }
        }
    }


}