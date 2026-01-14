package com.example.catalogofthings.presenter.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import com.example.catalogofthings.R
import com.example.catalogofthings.data.ImageConverter
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.databinding.NoteItemBinding


class ListNotesAdapter(
    private val onNoteClick: (NoteEntity) -> Unit,
    private val onNoteLongClick: (NoteEntity) -> Unit = {}
) : ListAdapter<NoteEntity, ListNotesAdapter.ListNotesViewHolder>(NoteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListNotesViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListNotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListNotesViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
        holder.itemView.setOnClickListener {
            onNoteClick(note)
        }

        holder.itemView.setOnLongClickListener {
            onNoteLongClick(note)
            true
        }
    }

    class ListNotesViewHolder (
        private val binding : NoteItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : NoteEntity) = with(binding) {
            val icon = if (item.isFolder)
                R.drawable.ic_folder
            else
                ImageConverter.toBitmap(item.icon)
                    ?: R.drawable.ic_no_image

            imageForElements.load(icon)

            if (icon == R.drawable.ic_no_image){
                imageForElements.imageTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(itemView.context, R.color.backgroundElements)
                )
            }
            else if (icon != R.drawable.ic_folder) imageForElements.imageTintList = null
            else imageForElements.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(itemView.context, R.color.accent)
            )
            textForCountNote.text = if (item.isFolder) item.childrenCount.toString() else ""
            textForTitleNote.text = item.title
        }
    }
}

class NoteDiffCallback : DiffUtil.ItemCallback<NoteEntity>() {
    override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
        return oldItem.noteId == newItem.noteId
    }

    override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(old: NoteEntity, new: NoteEntity): Any? {
        return null
    }

}