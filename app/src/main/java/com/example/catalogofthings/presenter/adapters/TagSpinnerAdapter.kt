package com.example.catalogofthings.presenter.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.catalogofthings.R
import com.example.catalogofthings.data.model.TagEntity
import com.google.android.material.button.MaterialButton

class TagSpinnerAdapter(
    context: Context,
    private val showEmptyOption: Boolean = true
) : ArrayAdapter<TagEntity?>(context, R.layout.tag_item) {

    private var tags: List<TagEntity> = emptyList()

    private var onTagFilterClick: (tag: TagEntity?) -> Unit = {}

    fun setOnTagFilterClick(onClick: (tag: TagEntity?) -> Unit) {
        onTagFilterClick = onClick
    }

    fun setData(newTags: List<TagEntity>) {
        tags = newTags
        clear()

        if (showEmptyOption) {
            add(null)
        }

        addAll(tags)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.tag_item, parent, false)

        val button = view.findViewById<MaterialButton>(R.id.tag_item)
        val tag = getItem(position)

        if (position == 0 && showEmptyOption) {
            button.text = "Не выбран"
        } else {
            tag?.let {
                button.text = it.title
                button.iconTint = ColorStateList.valueOf(it.color)
            }
        }

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.tag_item, parent, false)

        val button = view.findViewById<MaterialButton>(R.id.tag_item)
        val tag = getItem(position)

        if (position == 0 && showEmptyOption) {
            button.text = "Не выбран"
            button.setOnClickListener {
                onTagFilterClick(null)
            }
        } else {
            tag?.let {
                button.text = it.title
                button.iconTint = ColorStateList.valueOf(it.color)
                button.setOnClickListener {
                    onTagFilterClick(tag)
                }
            }
        }

        return view
    }

    override fun getItem(position: Int): TagEntity? {
        return if (position == 0 && showEmptyOption) {
            null
        } else {
            val adjustedPosition = if (showEmptyOption) position - 1 else position
            if (adjustedPosition >= 0 && adjustedPosition < tags.size) {
                tags[adjustedPosition]
            } else {
                null
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return getItem(position)?.tagId?.toLong() ?: -1L
    }

    fun getPosition(tagId: Int?): Int {
        val tag = tags.find { it.tagId == tagId }
        return getPosition(tag)
    }

    override fun isEnabled(position: Int): Boolean {
        return true
    }
}