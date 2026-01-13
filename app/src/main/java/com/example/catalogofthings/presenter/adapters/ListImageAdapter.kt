package com.example.catalogofthings.presenter.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.crossfade
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.example.catalogofthings.data.BitmapConverter
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

        fun bind(image: ImageEntity) = with(binding) {
            val bitmap = BitmapConverter.toBitmap(image.imageData)
                if (bitmap != null) {
                    imageItem.load(bitmap) {
                        crossfade(true)
                        transformations(RoundedCornersTransformation(16f))
                        size(width = 500, height = 500)
                    }
            }
        }
    }
}