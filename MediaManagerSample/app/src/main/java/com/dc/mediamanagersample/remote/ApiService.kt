package com.dc.mediamanagersample.remote

import com.dc.mediamanagersample.models.FileUploadResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {

    @Multipart
    @POST("upload")
    fun uploadFile(@Part filePart: MultipartBody.Part): Call<FileUploadResponse>

    @Multipart
    @POST("upload")
    fun uploadFiles(@Part fileParts: List<MultipartBody.Part>): Call<FileUploadResponse>

}