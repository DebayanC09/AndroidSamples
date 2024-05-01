package com.dc.mediamanagersample.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dc.mediamanagersample.databinding.ChildFileListBinding
import com.dc.mediamanagersample.utils.MediaManager


class FileListAdapter :
    ListAdapter<MediaManager.MediaData, FileListAdapter.ViewHolder>(DiffCallBack) {

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

    inner class ViewHolder(private val binding: ChildFileListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun setDataToViews(itemData: MediaManager.MediaData) {
            try {
                val bitmap: Bitmap? = MediaManager.FileUtil.fileToBitmap(itemData.file)
                binding.thumbnail.setImageBitmap(bitmap)
            } catch (e: Exception) {

            }

            var detailsText = ""
            detailsText = "${detailsText}\n${itemData.fileName}"
            detailsText = "${detailsText}\n${itemData.file.absoluteFile}"
            detailsText = "${detailsText}\n${itemData.fileSize}"
            detailsText = "${detailsText}\n${itemData.extension}"
            detailsText = "${detailsText}\n${itemData.mimeType}"

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