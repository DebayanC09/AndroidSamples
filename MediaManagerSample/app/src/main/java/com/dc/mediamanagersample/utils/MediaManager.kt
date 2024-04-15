package com.dc.mediamanagersample.utils

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MediaManager {

    companion object {
        const val ALL_IMAGE = "image/*"
        const val JPG = "image/jpg"
        const val JPEG = "image/jpeg"
        const val PNG = "image/png"
        const val GIF = "image/gif"
    }

    class PhotoPicker {

        private lateinit var picker: ActivityResultLauncher<PickVisualMediaRequest>

        companion object {

            val ImageAndVideo = ActivityResultContracts.PickVisualMedia.ImageAndVideo
            val ImageOnly = ActivityResultContracts.PickVisualMedia.ImageOnly
            val VideoOnly = ActivityResultContracts.PickVisualMedia.VideoOnly
            fun singleMimeType(mineType: String = ALL_IMAGE) =
                ActivityResultContracts.PickVisualMedia.SingleMimeType(mineType)
        }


        fun AppCompatActivity.registerSinglePicker(uriCallback: (result: Uri?) -> Unit) {
            picker =
                registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
                    uriCallback(uri)
                }
        }

        fun AppCompatActivity.registerMultiPicker(urisCallback: (result: List<Uri>?) -> Unit) {
            picker =
                registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris: List<Uri>? ->
                    urisCallback(uris)
                }
        }

        fun launch(visualMediaType: ActivityResultContracts.PickVisualMedia.VisualMediaType = ImageAndVideo) {
            if (::picker.isInitialized) {
                picker.launch(PickVisualMediaRequest(visualMediaType))
            }
        }

    }

    class FilePicker {
        private lateinit var picker: ActivityResultLauncher<Intent>

        fun AppCompatActivity.registerSinglePicker(uriCallback: (result: Uri?) -> Unit) {
            picker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    uriCallback(result.data?.data)
                }
        }

        fun AppCompatActivity.registerMultiPicker(urisCallback: (result: List<Uri>?) -> Unit) {
            picker = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    val uris: ArrayList<Uri> = ArrayList()
                    if (result.data?.clipData != null) {
                        val count = result.data?.clipData!!.itemCount
                        for (i in 0 until count) {
                            val uri = result.data?.clipData!!.getItemAt(i).uri
                            uris.add(uri)
                        }
                    } else {
                        val uri = result.data?.data
                        if (uri != null) {
                            uris.add(uri)
                        }
                    }

                    urisCallback(uris)
                }
        }

        fun launch(mediaType: String = ALL_IMAGE,allowMultiple : Boolean = false) {
            if (::picker.isInitialized) {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = mediaType
                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiple)
                }
                picker.launch(intent)
            }
        }
    }
}