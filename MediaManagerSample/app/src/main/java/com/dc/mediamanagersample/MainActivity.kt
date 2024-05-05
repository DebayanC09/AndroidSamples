package com.dc.mediamanagersample

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dc.mediamanagersample.databinding.ActivityMainBinding
import com.dc.mediamanagersample.views.FilePickerActivity
import com.dc.mediamanagersample.views.PhotoPickerActivity

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        setOnClickListener()

//        enableEdgeToEdge()
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
    }

    private fun setOnClickListener() {
        binding.apply {
            photoPickerButton.setOnClickListener {
                startActivity(Intent(this@MainActivity,PhotoPickerActivity::class.java))
            }

            filePickerButton.setOnClickListener {
                startActivity(Intent(this@MainActivity, FilePickerActivity::class.java))
            }
        }
    }
}