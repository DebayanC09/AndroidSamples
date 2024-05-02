package com.dc.mediamanagersample.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dc.mediamanagersample.databinding.ChildFileListBinding
import com.dc.mediamanagersample.utils.CommonUtils
import com.dc.mediamanagersample.utils.MediaManager
import com.dc.mediamanagersample.utils.RetrofitUtils
import com.dc.mediamanagersample.views.FilePickerActivity


class FileListAdapter :
    ListAdapter<MediaManager.MediaData, FileListAdapter.ViewHolder>(DiffCallBack) {

    private var uploadType: CommonUtils.UploadType = CommonUtils.UploadType.File

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

    override fun submitList(list: List<MediaManager.MediaData>?) {
        super.submitList(list?.let { ArrayList(it) })
    }

    fun setUploadType(uploadType: CommonUtils.UploadType) {
        this.uploadType = uploadType
    }

    inner class ViewHolder(private val binding: ChildFileListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setDataToViews(itemData: MediaManager.MediaData) {
            try {
                val bitmap: Bitmap? = when (uploadType) {
                    CommonUtils.UploadType.File -> {
                        itemData.file?.let { MediaManager.FileUtil.fileToBitmap(it) }
                    }

                    CommonUtils.UploadType.ByteArray -> {
                        itemData.byteArray?.let { MediaManager.FileUtil.byteArrayToBitmap(it) }
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

    private object DiffCallBack : DiffUtil.ItemCallback<MediaManager.MediaData>() {
        override fun areItemsTheSame(
            oldItem: MediaManager.MediaData, newItem: MediaManager.MediaData
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: MediaManager.MediaData, newItem: MediaManager.MediaData
        ): Boolean {
            return oldItem == newItem
        }
    }


}