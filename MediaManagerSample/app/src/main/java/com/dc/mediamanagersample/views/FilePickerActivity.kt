package com.dc.mediamanagersample.views


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dc.mediamanagersample.adapters.FileListAdapter
import com.dc.mediamanagersample.databinding.ActivityFilePickerBinding
import com.dc.mediamanagersample.models.FileUploadResponse
import com.dc.mediamanagersample.remote.RetrofitClient
import com.dc.mediamanagersample.utils.CommonUtils
import com.dc.mediamanagersample.utils.FilePicker
import com.dc.mediamanagersample.utils.FileUtil
import com.dc.mediamanagersample.utils.MediaData
import com.dc.mediamanagersample.utils.MediaType
import com.dc.mediamanagersample.utils.RetrofitUtils
import com.dc.mediamanagersample.utils.UploadType
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class FilePickerActivity : AppCompatActivity() {
    private val binding: ActivityFilePickerBinding by lazy {
        ActivityFilePickerBinding.inflate(layoutInflater)
    }

    private val mediaDataList: ArrayList<MediaData> = arrayListOf()
    private var uploadType: UploadType = UploadType.File
    private val fileListAdapter: FileListAdapter = FileListAdapter()

    private val filePicker: FilePicker =
        FilePicker(this@FilePickerActivity) { mediaData: List<MediaData> ->
            mediaDataList.clear()
            mediaDataList.addAll(mediaData)

            fileListAdapter.submitList(mediaDataList)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        initRecyclerView()
        setOnClickListener()

    }

    private fun setOnClickListener() {
        binding.uploadType.setOnCheckedChangeListener { _, checkedId ->

            when (checkedId) {
                binding.uploadFile.id -> {
                    uploadType = UploadType.File
                    fileListAdapter.setUploadType(uploadType)
                }

                binding.uploadByteArray.id -> {
                    uploadType = UploadType.ByteArray
                    fileListAdapter.setUploadType(uploadType)
                }
            }
        }


        binding.buttonLayout.setOnClickListener {
            showPriorityBottomSheet()
        }

        binding.deleteButton.setOnClickListener {
            deleteFiles()
        }
        binding.uploadButton.setOnClickListener {
            uploadFile()
            //uploadFiles()
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

        val partBody: MultipartBody.Part? = when (uploadType) {
            UploadType.File -> {
                data.file?.let {
                    RetrofitUtils.fileToPart(
                        name = "file",
                        file = it,
                        fileName = data.fileName
                    )
                }
            }

            UploadType.ByteArray -> {
                data.byteArray?.let {
                    RetrofitUtils.byteArrayToPart(
                        name = "file",
                        byteArray = it,
                        fileName = data.fileName
                    )
                }
            }
        }

        partBody?.let {
            RetrofitClient.invokeWithOutAuth().uploadFile(filePart = it).enqueue(object :
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
    }


    private fun uploadFiles() {
        val partList: ArrayList<MultipartBody.Part> = arrayListOf()

        mediaDataList.forEachIndexed { index, data ->
            data.file?.let { file: File ->
                val partBody: MultipartBody.Part = RetrofitUtils.fileToPart(
                    name = "file${index + 1}",
                    file = file,
                    fileName = data.fileName
                )
                partList.add(partBody)
            }
        }

        RetrofitClient.invokeWithOutAuth().uploadFiles(partList).enqueue(object :
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

                    4 -> {
                        launchMultiVideoPicker()
                    }

                    5 -> {
                        launchSingleFilePicker()
                    }

                    511 -> {
                        launchSingleFilePicker(mediaType = MediaType.ALL_IMAGE)
                    }

                    512 -> {
                        launchSingleFilePicker(mediaType = MediaType.JPG)
                    }

                    513 -> {
                        launchSingleFilePicker(mediaType = MediaType.JPEG)
                    }

                    514 -> {
                        launchSingleFilePicker(mediaType = MediaType.PNG)
                    }

                    515 -> {
                        launchSingleFilePicker(mediaType = MediaType.GIF)
                    }

                    521 -> {
                        launchSingleFilePicker(mediaType = MediaType.ALL_VIDEO)
                    }

                    522 -> {
                        launchSingleFilePicker(mediaType = MediaType.MP4)
                    }

                    531 -> {
                        launchSingleFilePicker(mediaType = MediaType.ALL_AUDIO)
                    }

                    532 -> {
                        launchSingleFilePicker(mediaType = MediaType.MP3)
                    }

                    533 -> {
                        launchSingleFilePicker(mediaType = MediaType.M4A)
                    }

                    541 -> {
                        launchSingleFilePicker(mediaType = MediaType.ALL_DOCUMENT)
                    }

                    542 -> {
                        launchSingleFilePicker(mediaType = MediaType.PDF)
                    }

                    543 -> {
                        launchSingleFilePicker(mediaType = MediaType.MS_WORD)
                    }

                    6 -> {
                        launchMultiFilePicker()
                    }

                    611 -> {
                        launchMultiFilePicker(mediaType = MediaType.ALL_IMAGE)
                    }

                    612 -> {
                        launchMultiFilePicker(mediaType = MediaType.JPG)
                    }

                    613 -> {
                        launchMultiFilePicker(mediaType = MediaType.JPEG)
                    }

                    614 -> {
                        launchMultiFilePicker(mediaType = MediaType.PNG)
                    }

                    615 -> {
                        launchMultiFilePicker(mediaType = MediaType.GIF)
                    }

                    621 -> {
                        launchMultiFilePicker(mediaType = MediaType.ALL_VIDEO)
                    }

                    622 -> {
                        launchMultiFilePicker(mediaType = MediaType.MP4)
                    }

                    631 -> {
                        launchMultiFilePicker(mediaType = MediaType.ALL_AUDIO)
                    }

                    632 -> {
                        launchMultiFilePicker(mediaType = MediaType.MP3)
                    }

                    633 -> {
                        launchMultiFilePicker(mediaType = MediaType.M4A)
                    }

                    641 -> {
                        launchMultiFilePicker(mediaType = MediaType.ALL_DOCUMENT)
                    }

                    642 -> {
                        launchMultiFilePicker(mediaType = MediaType.PDF)
                    }

                    643 -> {
                        launchMultiFilePicker(mediaType = MediaType.MS_WORD)
                    }
                }
            }
        }).show()
    }

    private fun deleteFiles() {
        for (mediaData in mediaDataList) {
            mediaData.file?.let {
                FileUtil.deleteFile(file = it)
            }
        }
        mediaDataList.clear()
        fileListAdapter.submitList(mediaDataList)
        Toast.makeText(this@FilePickerActivity, "Deleted", Toast.LENGTH_SHORT).show()
    }

    //region Picker Listeners
    private fun launchSingleFilePicker(mediaType: MediaType = MediaType.ALL) {
        filePicker.launchFilePicker(
            mediaType = mediaType,
            allowMultiple = false,
            uploadType = uploadType
        )
    }

    private fun launchMultiFilePicker(mediaType: MediaType = MediaType.ALL) {
        filePicker.launchFilePicker(
            mediaType = mediaType,
            allowMultiple = true,
            uploadType = uploadType
        )
    }

    private fun launchSingleImagePicker() {
        try {
            filePicker.launchImagePicker(uploadType = uploadType)
        } catch (e: Exception) {
            Toast.makeText(this@FilePickerActivity, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchMultiImagePicker() {
        try {
            filePicker.launchImagePicker(
                allowMultiple = true,
                uploadType = uploadType
            )
        } catch (e: Exception) {
            Toast.makeText(this@FilePickerActivity, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchSingleVideoPicker() {
        try {
            filePicker.launchVideoPicker(uploadType = uploadType)
        } catch (e: Exception) {
            Toast.makeText(this@FilePickerActivity, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchMultiVideoPicker() {
        try {
            filePicker.launchVideoPicker(
                allowMultiple = true,
                uploadType = uploadType
            )
        } catch (e: Exception) {
            Toast.makeText(this@FilePickerActivity, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    //endregion
}