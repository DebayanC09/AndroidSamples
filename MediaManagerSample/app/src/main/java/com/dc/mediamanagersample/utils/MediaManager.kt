package com.dc.mediamanagersample.utils

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MediaManager {
    class PhotoPicker(activity: AppCompatActivity, uriCallback: (result: Uri?) -> Unit) {

        companion object {
            const val ALL_IMAGE = "image/*"
            const val JPG = "image/jpg"
            const val JPEG = "image/jpeg"
            const val PNG = "image/png"
            const val GIF = "image/gif"

            val ImageAndVideo = ActivityResultContracts.PickVisualMedia.ImageAndVideo
            val ImageOnly = ActivityResultContracts.PickVisualMedia.ImageOnly
            val VideoOnly = ActivityResultContracts.PickVisualMedia.VideoOnly
            fun singleMimeType(mineType : String = ALL_IMAGE) = ActivityResultContracts.PickVisualMedia.SingleMimeType(mineType)
        }

        private lateinit var picker: ActivityResultLauncher<PickVisualMediaRequest>

        init {
            activity.registerPicker(uriCallback)
        }

        private fun AppCompatActivity.registerPicker(uriCallback: (result: Uri?) -> Unit) {
            picker =
                registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
                    uriCallback(uri)
                }
        }

        fun launch(visualMediaType: ActivityResultContracts.PickVisualMedia.VisualMediaType = ImageAndVideo) {
            if (::picker.isInitialized) {
                picker.launch(PickVisualMediaRequest(visualMediaType))
            }
        }

    }
}