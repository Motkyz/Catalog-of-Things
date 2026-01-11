package com.example.catalogofthings.presenter.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogofthings.R
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.databinding.NoteItemBinding


class ListNotesAdapter (private val onNoteClick: (NoteEntity) -> Unit,
                        private val onNoteLongClick: (NoteEntity) -> Unit = {}
) : ListAdapter<NoteEntity, ListNotesAdapter.ListNotesViewHolder>(NoteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListNotesViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListNotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListNotesViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }

    class ListNotesViewHolder (
        private val binding : NoteItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : NoteEntity) = with(binding) {
//            TODO val icon = if (item.isFolder) R.drawable.ic_folder else картинкаИзЗаписи ?: R.drawable.ic_no_image
//            imageForElements.setImageDrawable(ContextCompat.getDrawable(itemView.context, icon))
//            if (icon == R.drawable.ic_no_image){
//                imageForElements.imageTintList = ColorStateList.valueOf(
//                    ContextCompat.getColor(itemView.context, R.color.backgroundElements)
//                )
//            }
//            TODO textForCountNote.text = if (item.isFolder) getNumNotes(item.noteId) else ""
            textForTitleNote.text = item.title
        }

//    private fun getNumNotes(idNote : Int){
//        TODO сюда можно вставить функцию с высчитыванием чиселки для textForCountNote
//    }
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