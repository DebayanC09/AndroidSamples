package com.dc.mediamanagersample.views

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dc.mediamanagersample.databinding.ActivityPhotoPickerBinding

class PhotoPickerActivity : AppCompatActivity() {
    private val binding: ActivityPhotoPickerBinding by lazy {
        ActivityPhotoPickerBinding.inflate(layoutInflater)
    }

//    private val singlePicker: MediaManager.PhotoPicker = MediaManager.PhotoPicker().apply {
//        registerSinglePicker { uri: Uri? ->
//            setImage(uri)
//        }
//    }
//
//    private val multiPicker: MediaManager.PhotoPicker = MediaManager.PhotoPicker().apply {
//        registerMultiPicker { uris: List<Uri>? ->
//            println(uris)
//        }
//    }

    private fun setImage(uri: Uri?) {
        uri?.let {
            binding.image.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setOnClickListener()
    }

    private fun setOnClickListener() {
        binding.pickMediaButton.setOnClickListener {
            launchSinglePicker()
            launchMultiPicker()


        }
    }

    private fun launchMultiPicker() {
        //multiPicker.launch(MediaManager.PhotoPicker.ImageOnly)
    }

    private fun launchSinglePicker() {
//        singlePicker.launch(MediaManager.PhotoPicker.ImageAndVideo)
        //singlePicker.launch(MediaManager.PhotoPicker.ImageOnly)
//        singlePicker.launch(MediaManager.PhotoPicker.VideoOnly)
//
//        singlePicker.launch(MediaManager.PhotoPicker.singleMimeType(MediaManager.ALL_IMAGE))
//        singlePicker.launch(MediaManager.PhotoPicker.singleMimeType(MediaManager.JPG))
//        singlePicker.launch(MediaManager.PhotoPicker.singleMimeType(MediaManager.JPEG))
//        singlePicker.launch(MediaManager.PhotoPicker.singleMimeType(MediaManager.PNG))
//        singlePicker.launch(MediaManager.PhotoPicker.singleMimeType(MediaManager.GIF))
    }
}