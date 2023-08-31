package com.dc.facebooksample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dc.facebooksample.databinding.ActivityUserFeedBinding
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod


class UserFeedActivity : AppCompatActivity() {
    private val binding: ActivityUserFeedBinding by lazy {
        ActivityUserFeedBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/108772065599052/feed",
            null,
            HttpMethod.GET,
            { response ->
                val a = response.getJSONObject().toString()
                print(a)
            }
        ).executeAsync()
    }
}