package com.dc.mediamanagersample.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dc.mediamanagersample.databinding.ChildFileListBinding
import com.dc.mediamanagersample.utils.FileUtil
import com.dc.mediamanagersample.utils.MediaData
import com.dc.mediamanagersample.utils.UploadType


class FileListAdapter :
    ListAdapter<MediaData, FileListAdapter.ViewHolder>(DiffCallBack) {

    private var uploadType: UploadType = UploadType.File

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ChildFileListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setDataToViews(getItem(position))
    }

    override fun submitList(list: List<MediaData>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    fun setUploadType(uploadType: UploadType) {
        this.uploadType = uploadType
    }

    inner class ViewHolder(private val binding: ChildFileListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setDataToViews(itemData: MediaData) {
            try {
                val bitmap: Bitmap? = when (uploadType) {
                    UploadType.File -> {
                        itemData.file?.let { FileUtil.fileToBitmap(it) }
                    }

                    UploadType.ByteArray -> {
                        itemData.byteArray?.let { FileUtil.byteArrayToBitmap(it) }
                    }

                }
                binding.thumbnail.setImageBitmap(bitmap)
            } catch (_: Exception) {

            }

            var detailsText = ""
            detailsText = "${detailsText}\nMedia Name = ${itemData.fileName}"
            detailsText = "${detailsText}\nPath = ${itemData.file?.absoluteFile}"
            detailsText = "${detailsText}\nMedia Size = ${itemData.fileSize}"
            detailsText = "${detailsText}\nExtension = ${itemData.extension}"
            detailsText = "${detailsText}\nMIME = ${itemData.mimeType}"

            binding.details.text = detailsText
        }

    }

    private object DiffCallBack : DiffUtil.ItemCallback<MediaData>() {
        override fun areItemsTheSame(
            oldItem: MediaData, newItem: MediaData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: MediaData, newItem: MediaData
        ): Boolean {
            return oldItem == newItem
        }
    }


}