package com.dc.mediamanagersample.views

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dc.mediamanagersample.databinding.ActivityPhotoPickerBinding
import com.dc.mediamanagersample.utils.MediaManager

class PhotoPickerActivity : AppCompatActivity() {
    private val binding: ActivityPhotoPickerBinding by lazy {
        ActivityPhotoPickerBinding.inflate(layoutInflater)
    }

    private val picker: MediaManager.PhotoPicker =
        MediaManager.PhotoPicker(activity = this@PhotoPickerActivity, uriCallback = { uri ->
            println(uri)
            setImage(uri)
        })

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
//            picker.launch(MediaManager.PhotoPicker.ImageAndVideo)
//            picker.launch(MediaManager.PhotoPicker.ImageOnly)
//            picker.launch(MediaManager.PhotoPicker.VideoOnly)
//
//            picker.launch(MediaManager.PhotoPicker.singleMimeType(MediaManager.PhotoPicker.ALL_IMAGE))
//            picker.launch(MediaManager.PhotoPicker.singleMimeType(MediaManager.PhotoPicker.JPG))
//            picker.launch(MediaManager.PhotoPicker.singleMimeType(MediaManager.PhotoPicker.JPEG))
//            picker.launch(MediaManager.PhotoPicker.singleMimeType(MediaManager.PhotoPicker.PNG))
//            picker.launch(MediaManager.PhotoPicker.singleMimeType(MediaManager.PhotoPicker.GIF))


        }
    }
}