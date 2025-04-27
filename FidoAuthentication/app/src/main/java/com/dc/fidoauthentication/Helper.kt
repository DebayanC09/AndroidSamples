package com.dc.fidoauthentication

import android.content.Context
import android.os.Message
import android.preference.PreferenceManager
import android.util.Base64
import android.widget.Toast
import androidx.core.content.edit
import com.google.android.gms.fido.fido2.api.common.EC2Algorithm
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialCreationOptions
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialDescriptor
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialParameters
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialRequestOptions
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialRpEntity
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialType
import com.google.android.gms.fido.fido2.api.common.PublicKeyCredentialUserEntity
import java.security.SecureRandom

class Helper {
    private val KEY_HANDLE_PREF = "key_handle"
    private  val domain =
        "fido-test-android.firebaseapp.com" // Use your Firebase project domain here
    private val name = "Fido2Demo"


    fun storeKeyHandle(keyHandle: ByteArray,context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit {
            putString( KEY_HANDLE_PREF, Base64.encodeToString(keyHandle, Base64.DEFAULT))
        }
    }

    private fun loadKeyHandle(context: Context): ByteArray {
        val keyHandleBase64 = PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_HANDLE_PREF, null)
            ?: return byteArrayOf()
        return Base64.decode(keyHandleBase64, Base64.DEFAULT)
    }

    fun  generateLoginOption(context: Context): PublicKeyCredentialRequestOptions {
        // All the option parameters should come from the Relying Party / server
        val options = PublicKeyCredentialRequestOptions.Builder()
            .setRpId(domain)
            .setAllowList(
                listOf(
                    PublicKeyCredentialDescriptor(
                        PublicKeyCredentialType.PUBLIC_KEY.toString(),
                        loadKeyHandle(context),
                        null
                    )
                )
            )
            .setChallenge(challenge())
            .build()

        return options
    }

    // All the option parameters should come from the Relying Party / server
    fun generateRegisterOption(): PublicKeyCredentialCreationOptions {
        val options = PublicKeyCredentialCreationOptions.Builder()
            .setRp(PublicKeyCredentialRpEntity(domain, name, null)).setUser(
                PublicKeyCredentialUserEntity(
                    "demo@example.com".toByteArray(), "demo@example.com", "Demo", "Demo User"
                )
            ).setChallenge(challenge()).setParameters(
                listOf(
                    PublicKeyCredentialParameters(
                        PublicKeyCredentialType.PUBLIC_KEY.toString(), EC2Algorithm.ES256.algoValue
                    )
                )
            ).build()

        return options
    }

    private fun challenge(): ByteArray {
        val secureRandom = SecureRandom()
        val challenge = ByteArray(16)
        secureRandom.nextBytes(challenge)
        return challenge
    }



}