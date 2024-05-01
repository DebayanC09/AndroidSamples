package com.dc.mediamanagersample.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class FileUploadResponse {
    @SerializedName("originalname")
    @Expose
    var originalName: String? = null

    @SerializedName("filename")
    @Expose
    var fileName: String? = null

    @SerializedName("location")
    @Expose
    var location: String? = null
}