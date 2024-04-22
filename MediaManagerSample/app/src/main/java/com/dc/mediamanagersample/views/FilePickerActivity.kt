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

    private val singlePicker: MediaManager.FilePicker = MediaManager.FilePicker().apply {
        registerSinglePicker { uri: Uri? ->
            getMediaData(uri)
        }
    }

    private val multiPicker: MediaManager.FilePicker = MediaManager.FilePicker().apply {
        registerMultiPicker { uris: List<Uri>? ->
            getMediaDataList(uris)
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

    private fun launchMultiPicker() {
        multiPicker.launch(mediaType = MediaManager.ALL_IMAGE,allowMultiple = true)
    }

    private fun launchSinglePicker() {
        //singlePicker.launch(mediaType = MediaManager.ALL_IMAGE, allowMultiple = false)

    }

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

        println(mediaData.file.parentFile)


    }

}