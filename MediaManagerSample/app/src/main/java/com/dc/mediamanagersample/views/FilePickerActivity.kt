package com.dc.mediamanagersample.views

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dc.mediamanagersample.databinding.ActivityFilePickerBinding
import com.dc.mediamanagersample.utils.MediaManager
import com.dc.mediamanagersample.utils.MediaManager.FileUtil.uriToFile


class FilePickerActivity : AppCompatActivity() {
    private val binding: ActivityFilePickerBinding by lazy {
        ActivityFilePickerBinding.inflate(layoutInflater)
    }

    private val singlePicker: MediaManager.FilePicker = MediaManager.FilePicker().apply {
        registerSinglePicker { uri: Uri? ->
            getMediaData(uri)
        }
    }

    private fun getMediaData(uri: Uri?) {
        try {
            val mediaData: MediaManager.MediaData = uriToFile(uri = uri)
            println(mediaData)
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private val multiPicker: MediaManager.FilePicker = MediaManager.FilePicker().apply {
        registerMultiPicker { uris: List<Uri>? ->
            println(uris)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        setOnClickListener()

    }

    private fun setOnClickListener() {
        binding.pickFileButton.setOnClickListener {
            launchSinglePicker()
            launchMultiPicker()
        }
    }

    private fun launchMultiPicker() {
        //multiPicker.launch(mediaType = MediaManager.ALL_IMAGE,allowMultiple = true)
    }

    private fun launchSinglePicker() {
        singlePicker.launch(mediaType = MediaManager.ALL_IMAGE, allowMultiple = false)

    }


}