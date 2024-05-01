package com.dc.mediamanagersample.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.webkit.MimeTypeMap
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.database.getIntOrNull
import java.io.File
import java.io.FileOutputStream

class MediaManager {

    companion object {
        private const val IMAGE = "image"
        private const val VIDEO = "video"
        private const val AUDIO = "audio"
        private const val APPLICATION = "application"
    }

    data class MediaData(
        val fileName: String,
        val fileSize: Int,
        val file: File,
        val extension: String,
        val mimeType: String
    )

    enum class MediaType(val type: String) {
        ALL("*/*"),

        // Image MIME types
        ALL_IMAGE("${IMAGE}/*"),
        JPG("${IMAGE}/jpg"),
        JPEG("${IMAGE}/jpeg"),
        PNG("${IMAGE}/png"),
        GIF("${IMAGE}/gif"),

        // Video MIME types
        ALL_VIDEO("${VIDEO}/*"),
        MP4("${VIDEO}/mp4"),

        // Audio MIME types
        ALL_AUDIO("${AUDIO}/*"),
        MP3("${AUDIO}/mp3"),
        M4A("${AUDIO}/m4a"),

        // Document MIME types
        ALL_DOCUMENT("${APPLICATION}/*"),
        PDF("${APPLICATION}/pdf"),
        MS_WORD("${APPLICATION}/msword"),
    }

//    companion object {
//        const val ALL_IMAGE = "image/*"
//        const val JPG = "image/jpg"
//        const val JPEG = "image/jpeg"
//        const val PNG = "image/png"
//        const val GIF = "image/gif"
//
//        const val ALL_VIDEO = "video/*"
//        const val MP4 = "video/mp4"
//
//        const val ALL_AUDIO = "audio/*"
//        const val MP3 = "audio/mp3"
//        const val M4A = "audio/m4a"
//    }

//    class PhotoPicker {
//
//        private lateinit var picker: ActivityResultLauncher<PickVisualMediaRequest>
//
//        companion object {
//
//            val ImageAndVideo = ActivityResultContracts.PickVisualMedia.ImageAndVideo
//            val ImageOnly = ActivityResultContracts.PickVisualMedia.ImageOnly
//            val VideoOnly = ActivityResultContracts.PickVisualMedia.VideoOnly
//            fun singleMimeType(mineType: MediaType = MediaType.ALL_IMAGE) =
//                ActivityResultContracts.PickVisualMedia.SingleMimeType(mineType.type)
//        }
//
//
//        fun AppCompatActivity.registerSinglePicker(uriCallback: (result: Uri?) -> Unit) {
//            picker =
//                registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
//                    uriCallback(uri)
//                }
//        }
//
//        fun AppCompatActivity.registerMultiPicker(urisCallback: (result: List<Uri>?) -> Unit) {
//            picker =
//                registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia()) { uris: List<Uri>? ->
//                    urisCallback(uris)
//                }
//        }
//
//        fun launch(visualMediaType: ActivityResultContracts.PickVisualMedia.VisualMediaType = ImageAndVideo) {
//            if (::picker.isInitialized) {
//                picker.launch(PickVisualMediaRequest(visualMediaType))
//            }
//        }
//
//    }

    object FilePicker {

        fun AppCompatActivity.registerSinglePicker(uriCallback: (result: Uri?) -> Unit): ActivityResultLauncher<Intent> {
            return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                uriCallback(result.data?.data)
            }
        }

        fun AppCompatActivity.registerMultiPicker(urisCallback: (result: List<Uri>?) -> Unit): ActivityResultLauncher<Intent> {
            return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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

        fun ActivityResultLauncher<Intent>.launchFilePicker(
            mediaType: MediaType,
            allowMultiple: Boolean = false
        ) {

            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = mediaType.type
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiple)
            }
            launch(intent)

        }

        fun ActivityResultLauncher<Intent>.launchImagePicker(
            allowMultiple: Boolean = false
        ) {
            val intent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiple)
                }
            launch(intent)
        }

        fun ActivityResultLauncher<Intent>.launchVideoPicker(
            allowMultiple: Boolean = false
        ) {
            val intent =
                Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI).apply {
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, allowMultiple)
                }
            launch(intent)
        }

    }

    object FileUtil {

        fun fileToBitmap(file: File): Bitmap? {

            val mimeType = getMimeType(file)

            return if(mimeType?.contains(IMAGE) == true){
                BitmapFactory.decodeFile(file.absolutePath)
            }else if (mimeType?.contains(VIDEO) == true){
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(file.path)

                val bitmap = retriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
                retriever.release()

                bitmap
            }else{
                null
            }
        }

        fun deleteFile(file: File): Boolean {
            val fileToDelete = File(file.parentFile, file.name)

            if (fileToDelete.exists()) {
                return fileToDelete.delete()
            }
            return false
        }

        private fun AppCompatActivity.getFileName(uri: Uri): String? {
            val cursor = contentResolver.query(
                uri, arrayOf(
                    OpenableColumns.DISPLAY_NAME,
                ), null, null, null
            )
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    return cursor.getString(nameIndex)
                }
            } catch (e: Exception) {
                return null
            } finally {
                cursor?.close()
            }
            return null
        }

        private fun AppCompatActivity.getFileSize(uri: Uri): Int? {
            val cursor = contentResolver.query(
                uri, arrayOf(
                    OpenableColumns.SIZE,
                ), null, null, null
            )
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                    return cursor.getIntOrNull(sizeIndex)
                }
            } catch (e: Exception) {
                return null
            } finally {
                cursor?.close()
            }
            return null
        }

        fun AppCompatActivity.urisToFiles(
            uris: List<Uri>?,
            folderName: String = ""
        ): List<MediaData> {
            val list: ArrayList<MediaData> = arrayListOf()
            uris?.forEach { uri ->
                try {
                    val mediaData: MediaData = uriToFile(uri = uri, folderName = folderName)
                    list.add(mediaData)
                } catch (e: Exception) {
                    throw Exception(e.message)
                }
            }
            return list
        }

        fun AppCompatActivity.uriToFile(uri: Uri?, folderName: String = ""): MediaData {

            uri?.let { uriData ->

                val fileName: String = getFileName(uriData) ?: throw Exception("File name is null")

                var fileSize: Int? = getFileSize(uriData)

                val directoryPath: File = getFolderPath(context = this, folderName = folderName)
                    ?: throw Exception("File not created")


                val tempFile: File = File(directoryPath, fileName).apply {
                    createNewFile()
                }

                val outputStream = FileOutputStream(tempFile)

                val inputStream = contentResolver.openInputStream(uriData)

                if (fileSize == null) {
                    val availableBytes: Int? = inputStream?.available()
                    fileSize = availableBytes ?: 8192
                }


                inputStream?.let { stream ->
                    val buf = ByteArray(fileSize)
                    var length: Int
                    while (stream.read(buf).also { length = it } > 0) {
                        outputStream.write(buf, 0, length)
                    }
                }
                inputStream?.close()

                val extension: String = getFileExtension(file = tempFile)
                val mimeType: String = getMimeType(file = tempFile) ?: ""

                return MediaData(
                    fileName = fileName,
                    fileSize = fileSize,
                    file = tempFile,
                    extension = extension,
                    mimeType = mimeType
                )
            } ?: run {
                throw Exception("URI is null")
            }


        }

        private fun getMimeType(file: File): String? {
            val mimeTypeMap = MimeTypeMap.getSingleton()
            val extension: String = getFileExtension(file = file)
            return mimeTypeMap.getMimeTypeFromExtension(extension)
        }

        private fun getFileExtension(file: File): String {
            return MimeTypeMap.getFileExtensionFromUrl(file.path)
        }

        private fun getFolderPath(context: Context, folderName: String): File? {
            val parentDirectory = "MediaManager"
            val directoryPath: File = if (folderName.isNotEmpty()) {
                File(context.cacheDir, "${parentDirectory}/${folderName}")
            } else {
                File(context.cacheDir, parentDirectory)
            }

            if (!directoryPath.exists()) {
                return if (directoryPath.mkdirs()) {
                    directoryPath
                } else {
                    null
                }
            }

            return directoryPath
        }


    }
}