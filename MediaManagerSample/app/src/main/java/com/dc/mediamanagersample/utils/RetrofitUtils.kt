package com.dc.mediamanagersample.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

object RetrofitUtils {
    fun fileToPart(name : String,file: File,fileName : String): MultipartBody.Part {
        val requestBody : RequestBody = fileToRequestBody(file)
        return MultipartBody.Part.createFormData(name,fileName, requestBody)
    }

    private fun fileToRequestBody(file: File): RequestBody {
        return file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
    }
}