package com.example.catalogofthings.presenter.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogofthings.data.model.ImageEntity
import com.example.catalogofthings.databinding.ImageInNoteItemBinding

class ListImagesAdapter(
    private val onImageClick: (ImageEntity) -> Unit = {}
) : RecyclerView.Adapter<ListImagesAdapter.ImageViewHolder>() {

    private val images = mutableListOf<ImageEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ImageInNoteItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = images[position]
        holder.bind(image)
        holder.itemView.setOnClickListener { onImageClick(image) }
    }

    override fun getItemCount(): Int = images.size

    fun submitList(newList: List<ImageEntity>?) {
        images.clear()
        if (newList != null) {
            images.addAll(newList)
        }
        notifyDataSetChanged()
    }

    class ImageViewHolder(
        private val binding: ImageInNoteItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(image: ImageEntity) = with(binding.root) {
            val bitmap = BitmapFactory.decodeByteArray(image.imageData, 0, image.imageData.size)

            if (bitmap != null) {
                setImageBitmap(bitmap)
            }
        }
    }
}