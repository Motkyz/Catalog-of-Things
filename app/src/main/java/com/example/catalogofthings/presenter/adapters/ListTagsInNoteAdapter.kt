package com.example.catalogofthings.presenter.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.catalogofthings.data.model.TagEntity
import com.example.catalogofthings.databinding.TagItemBinding

class ListTagsInNoteAdapter (
    private val onTagClick: (TagEntity) -> Unit
) : RecyclerView.Adapter<ListTagsInNoteAdapter.ListTagsInNoteViewHolder>() {

    private val list : MutableList<TagEntity> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListTagsInNoteViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = TagItemBinding.inflate(inflater, parent, false)
        return ListTagsInNoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListTagsInNoteViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)

        holder.itemView.setOnClickListener { onTagClick(item) }    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun submitList(list: List<TagEntity>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class ListTagsInNoteViewHolder (
        private val binding : TagItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind (item : TagEntity)= with(binding) {
            tagItem.text = item.title
            tagItem.iconTint = ColorStateList.valueOf(item.color)
        }
    }
}