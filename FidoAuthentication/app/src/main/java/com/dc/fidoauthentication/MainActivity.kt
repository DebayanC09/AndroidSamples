package com.dc.fidoauthentication

import android.app.PendingIntent
import android.os.Bundle
import android.util.Base64
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dc.fidoauthentication.databinding.ActivityMainBinding
import com.google.android.gms.fido.Fido
import com.google.android.gms.fido.fido2.Fido2ApiClient
import com.google.android.gms.fido.fido2.api.common.*
import com.google.android.gms.tasks.Task

class MainActivity : AppCompatActivity() {

    private lateinit var registerLauncher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var signLauncher: ActivityResultLauncher<IntentSenderRequest>

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val helper: Helper by lazy {
        Helper()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupLaunchers()

        binding.registerFido2Button.setOnClickListener { registerFido2() }
        binding.signFido2Button.setOnClickListener { signFido2() }
    }

    private fun setupLaunchers() {
        registerLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                val data = result.data
                if (result.resultCode == RESULT_OK && data != null) {
                    if (data.hasExtra(Fido.FIDO2_KEY_CREDENTIAL_EXTRA)) {
                        val byteArray: ByteArray? =
                            data.getByteArrayExtra(Fido.FIDO2_KEY_CREDENTIAL_EXTRA)


                        byteArray?.let {
                            try {
                                val credential: PublicKeyCredential =
                                    PublicKeyCredential.deserializeFromBytes(it)
                                val response = credential.response
                                val rawId : ByteArray? = credential.rawId


                                // Handle based on the type of response
                                when (response) {
                                    is AuthenticatorAttestationResponse -> {
                                        if (rawId != null) {
                                            handleRegisterResponse(response,rawId)
                                            showToast("Registration successful")
                                        }
                                    }

                                    is AuthenticatorErrorResponse -> {
                                        handleErrorResponse(response)
                                        showToast("Authentication error: ${response.errorMessage}")
                                    }


                                    else -> {
                                        showToast("Unexpected response received")
                                    }
                                }
                            } catch (e: Exception) {
                                showToast("Error deserializing credential: ${e.localizedMessage}")
                            }
                        } ?: run {
                            showToast("Received null byte array")
                        }
                    } else {
                        showToast("Credential extra missing")
                    }
                } else {
                    showToast("Something Went wrong")
                }
            }

        signLauncher =
            registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
                val data = result.data
                if (result.resultCode == RESULT_OK && data != null) {
                    if (data.hasExtra(Fido.FIDO2_KEY_CREDENTIAL_EXTRA)) {
                        val byteArray: ByteArray? =
                            data.getByteArrayExtra(Fido.FIDO2_KEY_CREDENTIAL_EXTRA)

                        byteArray?.let {
                            try {
                                val credential: PublicKeyCredential =
                                    PublicKeyCredential.deserializeFromBytes(it)
                                val response = credential.response
                                val rawId : ByteArray? = credential.rawId

                                when (response) {
                                    is AuthenticatorErrorResponse -> {
                                        handleErrorResponse(response)
                                        showToast("Authentication error: ${response.errorMessage}")
                                    }

                                    is AuthenticatorAssertionResponse -> {
                                        if (rawId != null) {
                                            handleSignResponse(response,rawId)
                                            showToast("Login successful")
                                        }
                                    }

                                    else -> {
                                        showToast("Unexpected response received")
                                    }
                                }
                            } catch (e: Exception) {
                                showToast("Error deserializing credential: ${e.localizedMessage}")
                            }
                        } ?: run {
                            showToast("Received null byte array")
                        }
                    } else {
                        showToast("Credential extra missing")
                    }
                } else {
                    showToast("Something Went wrong")
                }
            }
    }

    private fun registerFido2() {
        val options = helper.generateRegisterOption()

        val fido2ApiClient: Fido2ApiClient = Fido.getFido2ApiClient(applicationContext)
        val pendingIntentTask: Task<PendingIntent> =
            fido2ApiClient.getRegisterPendingIntent(options)

        pendingIntentTask.addOnSuccessListener { pendingIntent ->
            val request = IntentSenderRequest.Builder(pendingIntent.intentSender).build()
            registerLauncher.launch(request)
        }.addOnFailureListener { e ->
            e.printStackTrace()
        }
    }

    private fun signFido2() {
        val options = helper.generateLoginOption(this)

        val fido2ApiClient = Fido.getFido2ApiClient(applicationContext)
        val pendingIntentTask: Task<PendingIntent> = fido2ApiClient.getSignPendingIntent(options)

        pendingIntentTask.addOnSuccessListener { pendingIntent ->
            val request = IntentSenderRequest.Builder(pendingIntent.intentSender).build()
            signLauncher.launch(request)
        }.addOnFailureListener { e ->
            e.printStackTrace()
        }
    }

    private fun handleRegisterResponse(response: AuthenticatorAttestationResponse,rawId : ByteArray) {


        val rawIdBase64 = Base64.encodeToString(rawId, Base64.DEFAULT)
        val clientDataJson = String(response.clientDataJSON, Charsets.UTF_8)
        //val attestationObjectBase64 = Base64.encodeToString(response.attestationObject, Base64.DEFAULT)

        helper.storeKeyHandle(rawId, this)
        binding.signFido2Button.isEnabled = true

        val registerFido2Result = """
                Authenticator Register Response

                rawIdBase64:
                $rawIdBase64

                clientDataJSON:
                $clientDataJson

            """.trimIndent()

        binding.resultText.text = registerFido2Result

    }

    private fun handleSignResponse(response: AuthenticatorAssertionResponse,rawId: ByteArray) {

        val rawIdBase64 = Base64.encodeToString(rawId, Base64.DEFAULT)
        val clientDataJson = String(response.clientDataJSON, Charsets.UTF_8)
        val authenticatorDataBase64 =
            Base64.encodeToString(response.authenticatorData, Base64.DEFAULT)
        val signatureBase64 = Base64.encodeToString(response.signature, Base64.DEFAULT)

        val signFido2Result = """
                Authenticator Login Response

                rawIdBase64:
                $rawIdBase64

                clientDataJSON:
                $clientDataJson

                authenticatorDataBase64:
                $authenticatorDataBase64

                signatureBase64:
                $signatureBase64
            """.trimIndent()

        binding.resultText.text = signFido2Result

    }

    private fun handleErrorResponse(credential: AuthenticatorErrorResponse) {


        val errorName = credential.errorCode.name
        val errorMessage = credential.errorMessage

        val registerFidoResult = """
                An Error Occurred

                Error Name:
                $errorName

                Error Message:
                $errorMessage
            """.trimIndent()

        binding.resultText.text = registerFidoResult

    }
}