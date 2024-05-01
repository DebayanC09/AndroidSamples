package com.dc.mediamanagersample.views


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dc.mediamanagersample.adapters.FileListAdapter
import com.dc.mediamanagersample.databinding.ActivityFilePickerBinding
import com.dc.mediamanagersample.models.FileUploadResponse
import com.dc.mediamanagersample.remote.RetrofitClient
import com.dc.mediamanagersample.utils.CommonUtils
import com.dc.mediamanagersample.utils.MediaManager
import com.dc.mediamanagersample.utils.MediaManager.FilePicker.launchFilePicker
import com.dc.mediamanagersample.utils.MediaManager.FilePicker.launchImagePicker
import com.dc.mediamanagersample.utils.MediaManager.FilePicker.launchVideoPicker
import com.dc.mediamanagersample.utils.MediaManager.FilePicker.registerMultiPicker
import com.dc.mediamanagersample.utils.MediaManager.FilePicker.registerSinglePicker
import com.dc.mediamanagersample.utils.MediaManager.FileUtil.uriToFile
import com.dc.mediamanagersample.utils.MediaManager.FileUtil.urisToFiles
import com.dc.mediamanagersample.utils.RetrofitUtils
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FilePickerActivity : AppCompatActivity() {
    private val mediaDataList: ArrayList<MediaManager.MediaData> = arrayListOf()
    private val binding: ActivityFilePickerBinding by lazy {
        ActivityFilePickerBinding.inflate(layoutInflater)
    }

    private val fileListAdapter: FileListAdapter = FileListAdapter()


    private val singlePicker: ActivityResultLauncher<Intent> = registerSinglePicker { uri: Uri? ->
        getMediaData(uri)
    }


    private val multiPicker = registerMultiPicker { uris: List<Uri>? ->
        getMediaDataList(uris)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        initRecyclerView()
        setOnClickListener()

    }

    private fun setOnClickListener() {
        binding.buttonLayout.setOnClickListener {
            showPriorityBottomSheet()
        }

        binding.deleteButton.setOnClickListener {
            deleteFiles()
        }
        binding.uploadButton.setOnClickListener {
            uploadFile()
        }
    }

    private fun initRecyclerView() {
        binding.filesListView.apply {
            layoutManager = LinearLayoutManager(this@FilePickerActivity)
            setHasFixedSize(true)
            adapter = fileListAdapter
        }
    }

    private fun uploadFile() {

        val data = mediaDataList.first()

        val partBody: MultipartBody.Part =
            RetrofitUtils.fileToPart(name = "file", file = data.file, fileName = data.fileName)


        RetrofitClient.invokeWithOutAuth().uploadFile(partBody).enqueue(object :
            Callback<FileUploadResponse> {
            override fun onResponse(
                call: Call<FileUploadResponse>,
                response: Response<FileUploadResponse>
            ) {
                deleteFiles()
            }

            override fun onFailure(call: Call<FileUploadResponse>, t: Throwable) {
                deleteFiles()
            }

        })
    }

    private fun showPriorityBottomSheet() {
        CustomBottomSheet(this, CommonUtils.filePickerList).setOnClickListener(object :
            CustomBottomSheet.BottomSheetClickListener {
            override fun onClick(model: CustomBottomSheet.BottomSheetModel) {
                when (model.id) {
                    1 -> {
                        launchSingleImagePicker()
                    }

                    2 -> {
                        launchMultiImagePicker()
                    }

                    3 -> {
                        launchSingleVideoPicker()
                    }

                    5 -> {
                        launchSingleFilePicker()
                    }

                    511 -> {
                        launchSingleFilePicker(mediaType = MediaManager.MediaType.ALL_IMAGE)
                    }

                    512 -> {
                        launchSingleFilePicker(mediaType = MediaManager.MediaType.JPG)
                    }

                    513 -> {
                        launchSingleFilePicker(mediaType = MediaManager.MediaType.JPEG)
                    }

                    514 -> {
                        launchSingleFilePicker(mediaType = MediaManager.MediaType.PNG)
                    }

                    515 -> {
                        launchSingleFilePicker(mediaType = MediaManager.MediaType.GIF)
                    }

                    521 -> {
                        launchSingleFilePicker(mediaType = MediaManager.MediaType.ALL_VIDEO)
                    }

                    522 -> {
                        launchSingleFilePicker(mediaType = MediaManager.MediaType.MP4)
                    }

                    531 -> {
                        launchSingleFilePicker(mediaType = MediaManager.MediaType.ALL_AUDIO)
                    }

                    532 -> {
                        launchSingleFilePicker(mediaType = MediaManager.MediaType.MP3)
                    }

                    533 -> {
                        launchSingleFilePicker(mediaType = MediaManager.MediaType.M4A)
                    }

                    541 -> {
                        launchSingleFilePicker(mediaType = MediaManager.MediaType.ALL_DOCUMENT)
                    }

                    542 -> {
                        launchSingleFilePicker(mediaType = MediaManager.MediaType.PDF)
                    }
                }
            }
        }).show()
    }

    private fun deleteFiles() {
        for (mediaData in mediaDataList) {
            MediaManager.FileUtil.deleteFile(mediaData.file)
        }
        mediaDataList.clear()
        fileListAdapter.submitList(mediaDataList)
        Toast.makeText(this@FilePickerActivity, "Deleted", Toast.LENGTH_SHORT).show()
    }

    //region Picker Listeners
    private fun launchSingleFilePicker(mediaType: MediaManager.MediaType = MediaManager.MediaType.ALL) {
        singlePicker.launchFilePicker(
            mediaType = mediaType,
            allowMultiple = false
        )
    }

    private fun launchMultiFilePicker() {
        multiPicker.launchFilePicker(
            mediaType = MediaManager.MediaType.ALL_IMAGE,
            allowMultiple = true
        )
    }

    private fun launchSingleImagePicker(mediaType: MediaManager.MediaType = MediaManager.MediaType.ALL_IMAGE) {
        try {
            singlePicker.launchImagePicker(mediaType = mediaType)
        } catch (e: Exception) {
            Toast.makeText(this@FilePickerActivity, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchMultiImagePicker(mediaType: MediaManager.MediaType = MediaManager.MediaType.ALL_IMAGE) {
        try {
            multiPicker.launchImagePicker(
                mediaType = mediaType,
                allowMultiple = true
            )
        } catch (e: Exception) {
            Toast.makeText(this@FilePickerActivity, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchSingleVideoPicker() {
        try {
            singlePicker.launchVideoPicker(mediaType = MediaManager.MediaType.ALL_VIDEO)
        } catch (e: Exception) {
            Toast.makeText(this@FilePickerActivity, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchMultiVideoPicker() {
        try {
            multiPicker.launchVideoPicker(
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
            mediaDataList.clear()
            val file = uriToFile(uri = uri)
            mediaDataList.add(file)

            fileListAdapter.submitList(mediaDataList)
        } catch (e: Exception) {
            Toast.makeText(this@FilePickerActivity, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getMediaDataList(uris: List<Uri>?) {
        try {
            mediaDataList.clear()
            val fileList: List<MediaManager.MediaData> = urisToFiles(uris = uris)
            mediaDataList.addAll(fileList)

            fileListAdapter.submitList(mediaDataList)
        } catch (e: Exception) {
            Toast.makeText(this@FilePickerActivity, e.message, Toast.LENGTH_SHORT).show()
        }
    }

}