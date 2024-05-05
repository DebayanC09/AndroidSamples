package com.dc.mediamanagersample.views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dc.mediamanagersample.databinding.CustomBottomSheetChildBinding
import com.dc.mediamanagersample.databinding.CustomBottomSheetParentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class CustomBottomSheet(
    private val context: Context,
    private val list: List<BottomSheetModel>
) {
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var onClickListener: BottomSheetClickListener

    init {
        setBottomSheetLayout()
    }

    private fun setBottomSheetLayout() {
        val binding = CustomBottomSheetParentBinding.inflate(LayoutInflater.from(context))
        bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(binding.root)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = RecyclerViewAdapter(list)
    }

    fun show(): CustomBottomSheet {
        if (::bottomSheetDialog.isInitialized) {
            bottomSheetDialog.show()
        }
        return this
    }

    fun setOnClickListener(onClickListener: BottomSheetClickListener): CustomBottomSheet {
        this.onClickListener = onClickListener
        return this
    }

    inner class RecyclerViewAdapter(private val list: List<BottomSheetModel>) :
        RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewViewHolder {

            return RecyclerViewViewHolder(
                CustomBottomSheetChildBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(holder: RecyclerViewViewHolder, position: Int) {
            holder.setDataToViews(position)
            holder.onClickListener()
        }

        inner class RecyclerViewViewHolder(private val binding: CustomBottomSheetChildBinding) :
            RecyclerView.ViewHolder(binding.root) {
            fun setDataToViews(position: Int) {

                val item = list[position]

                if (item.header.isNullOrEmpty()) {
                    binding.headerText.text = ""
                    binding.childText.text = list[position].name

                    binding.headerText.visibility = View.GONE
                    binding.childText.visibility = View.VISIBLE
                } else {
                    binding.headerText.text = list[position].header
                    binding.childText.text = ""

                    binding.headerText.visibility = View.VISIBLE
                    binding.childText.visibility = View.GONE
                }
            }

            fun onClickListener() {
                binding.childText.setOnClickListener {
                    val bottomSheetModel = list[adapterPosition]
                    onClickListener.onClick(bottomSheetModel)
                    bottomSheetDialog.dismiss()
                }
            }

        }
    }

    interface BottomSheetClickListener {
        fun onClick(model: BottomSheetModel)
    }

    class BottomSheetModel(
        val id: Int? = null,
        val name: String? = null,
        val header: String? = null
    )
}