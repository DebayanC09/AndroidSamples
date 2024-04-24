package com.dc.mediamanagersample.views

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dc.mediamanagersample.databinding.ActivityFilePickerBinding
import com.dc.mediamanagersample.utils.MediaManager
import com.dc.mediamanagersample.utils.MediaManager.FileUtil.uriToFile
import com.dc.mediamanagersample.utils.MediaManager.FileUtil.urisToFiles


class FilePickerActivity : AppCompatActivity() {
    private lateinit var mediaDataList: List<MediaManager.MediaData>
    private lateinit var mediaData: MediaManager.MediaData
    private val binding: ActivityFilePickerBinding by lazy {
        ActivityFilePickerBinding.inflate(layoutInflater)
    }

    private val singleFilePicker: MediaManager.FilePicker = MediaManager.FilePicker().apply {
        registerSinglePicker { uri: Uri? ->
            getMediaData(uri)
        }
    }

    private val multiFilePicker: MediaManager.FilePicker = MediaManager.FilePicker().apply {
        registerMultiPicker { uris: List<Uri>? ->
            getMediaDataList(uris)
        }
    }

    private val singleImagePicker: MediaManager.FilePicker = MediaManager.FilePicker().apply {
        registerSinglePicker { uri: Uri? ->
            //getMediaData(uri)
        }
    }

    private val multiImagePicker: MediaManager.FilePicker = MediaManager.FilePicker().apply {
        registerMultiPicker { uris: List<Uri>? ->
            //getMediaDataList(uris)
        }
    }

    private val singleVideoPicker: MediaManager.FilePicker = MediaManager.FilePicker().apply {
        registerSinglePicker { uri: Uri? ->
            //getMediaData(uri)
        }
    }

    private val multiVideoPicker: MediaManager.FilePicker = MediaManager.FilePicker().apply {
        registerMultiPicker { uris: List<Uri>? ->
            //getMediaDataList(uris)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        setOnClickListener()

    }

    private fun setOnClickListener() {
        binding.pickSingleFileButton.setOnClickListener {
            launchSingleFilePicker()
        }

        binding.pickMultiFileButton.setOnClickListener {
            launchMultiFilePicker()
        }

        binding.pickSingleImageButton.setOnClickListener {
            launchSingleImagePicker()
        }

        binding.pickMultiImageButton.setOnClickListener {
            launchMultiImagePicker()
        }

        binding.pickSingleVideoButton.setOnClickListener {
            launchSingleVideoPicker()
        }

        binding.pickMultiVideoButton.setOnClickListener {
            launchMultiVideoPicker()
        }

        binding.deleteButton.setOnClickListener {
            deleteFile()
        }
    }

    private fun deleteFile() {
        if (::mediaData.isInitialized) {
            val result = MediaManager.FileUtil.deleteFile(mediaData.file)
            Toast.makeText(this@FilePickerActivity, result.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    //region Picker Listeners
    private fun launchSingleFilePicker() {
        singleFilePicker.launchFilePicker(
            mediaType = MediaManager.MediaType.ALL_IMAGE,
            allowMultiple = false
        )
    }

    private fun launchMultiFilePicker() {
        multiFilePicker.launchFilePicker(
            mediaType = MediaManager.MediaType.ALL_IMAGE,
            allowMultiple = true
        )
    }

    private fun launchSingleImagePicker() {
        try {
            singleImagePicker.launchImagePicker(mediaType = MediaManager.MediaType.ALL_IMAGE)
        } catch (e: Exception) {
            Toast.makeText(this@FilePickerActivity, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchMultiImagePicker() {
        try {
            multiImagePicker.launchImagePicker(
                mediaType = MediaManager.MediaType.ALL_IMAGE,
                allowMultiple = true
            )
        } catch (e: Exception) {
            Toast.makeText(this@FilePickerActivity, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchSingleVideoPicker() {
        try {
            singleVideoPicker.launchVideoPicker(mediaType = MediaManager.MediaType.ALL_VIDEO)
        } catch (e: Exception) {
            Toast.makeText(this@FilePickerActivity, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchMultiVideoPicker() {
        try {
            multiVideoPicker.launchVideoPicker(
                mediaType = MediaManager.MediaType.ALL_VIDEO,
                allowMultiple = true
            )
        } catch (e: Exception) {
            Toast.makeText(this@FilePickerActivity, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    //endregion

    private fun getMediaData(uri: Uri?) {
        try {
            mediaData = uriToFile(uri = uri)
            setDataToViews(mediaData)
        } catch (e: Exception) {
            Toast.makeText(this@FilePickerActivity, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getMediaDataList(uris: List<Uri>?) {
        mediaDataList = urisToFiles(uris = uris)
    }

    private fun setDataToViews(mediaData: MediaManager.MediaData) {
        val bitmap: Bitmap? = MediaManager.FileUtil.fileToBitmap(mediaData.file)
        binding.image.setImageBitmap(bitmap)

        var detailsText = ""
        detailsText = "${detailsText}\n${mediaData.fileName}"
        detailsText = "${detailsText}\n${mediaData.file.absoluteFile}"
        detailsText = "${detailsText}\n${mediaData.fileSize}"
        detailsText = "${detailsText}\n${mediaData.extension}"
        detailsText = "${detailsText}\n${mediaData.mimeType}"

        binding.details.text = detailsText

    }

}