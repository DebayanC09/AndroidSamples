package com.dc.mediamanagersample.views

import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dc.mediamanagersample.adapters.FileListAdapter
import com.dc.mediamanagersample.databinding.ActivityPhotoPickerBinding
import com.dc.mediamanagersample.models.FileUploadResponse
import com.dc.mediamanagersample.remote.RetrofitClient
import com.dc.mediamanagersample.utils.CommonUtils
import com.dc.mediamanagersample.utils.FileUtil
import com.dc.mediamanagersample.utils.MediaData
import com.dc.mediamanagersample.utils.PhotoPicker
import com.dc.mediamanagersample.utils.RetrofitUtils
import com.dc.mediamanagersample.utils.UploadType
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PhotoPickerActivity : AppCompatActivity() {
    private val binding: ActivityPhotoPickerBinding by lazy {
        ActivityPhotoPickerBinding.inflate(layoutInflater)
    }

    private val mediaDataList: ArrayList<MediaData> = arrayListOf()
    private var uploadType: UploadType = UploadType.File
    private val fileListAdapter: FileListAdapter = FileListAdapter()

    private val photoPicker: PhotoPicker =
        PhotoPicker(this@PhotoPickerActivity) { mediaData: List<MediaData> ->
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
            layoutManager = LinearLayoutManager(this@PhotoPickerActivity)
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
        CustomBottomSheet(this, CommonUtils.photoPickerList).setOnClickListener(object :
            CustomBottomSheet.BottomSheetClickListener {
            override fun onClick(model: CustomBottomSheet.BottomSheetModel) {
                when (model.id) {
                    1 -> {
                        launchSinglePicker(visualMediaType = PhotoPicker.ImageOnly)
                    }

                    2 -> {
                        launchMultiPicker(visualMediaType = PhotoPicker.ImageOnly)
                    }

                    3 -> {
                        launchSinglePicker(visualMediaType = PhotoPicker.VideoOnly)
                    }

                    4 -> {
                        launchMultiPicker(visualMediaType = PhotoPicker.VideoOnly)
                    }

                    5 -> {
                        launchSinglePicker(visualMediaType = PhotoPicker.ImageAndVideo)
                    }

                    6 -> {
                        launchMultiPicker(visualMediaType = PhotoPicker.ImageAndVideo)
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
        Toast.makeText(this@PhotoPickerActivity, "Deleted", Toast.LENGTH_SHORT).show()
    }

    //region Picker Listeners

    private fun launchSinglePicker(visualMediaType: ActivityResultContracts.PickVisualMedia.VisualMediaType) {
        try {
            photoPicker.launchSinglePicker(
                visualMediaType = visualMediaType,
                uploadType = uploadType
            )
        } catch (e: Exception) {
            Toast.makeText(this@PhotoPickerActivity, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun launchMultiPicker(visualMediaType: ActivityResultContracts.PickVisualMedia.VisualMediaType) {
        photoPicker.launchMultiPicker(
            visualMediaType = visualMediaType,
            uploadType = uploadType
        )
    }

    //endregion
}