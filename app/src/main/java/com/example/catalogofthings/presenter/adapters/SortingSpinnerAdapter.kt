package com.example.catalogofthings.presenter.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.catalogofthings.R
import com.example.catalogofthings.enums.SortingVariantsEnum
import com.google.android.material.button.MaterialButton

class SortingSpinnerAdapter(
    context: Context
) : ArrayAdapter<SortingVariantsEnum>(context, R.layout.sorting_variant, SortingVariantsEnum.entries.toTypedArray()) {

    private var onVariantClick: ((SortingVariantsEnum) -> Unit) = {}

    fun setOnVariantClick(onClick: (SortingVariantsEnum) -> Unit) {
        onVariantClick = onClick
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent, false)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent, true)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup, isDropdown: Boolean): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.sorting_variant, parent, false)

        val button = view.findViewById<MaterialButton>(R.id.sorting_variant)
        val variant = getItem(position)

        variant?.let {
            button.text = it.variant

            if (isDropdown) {
                button.setOnClickListener {
                    onVariantClick(variant)
                }
            }
        }

        return view
    }

    override fun getCount(): Int {
        return SortingVariantsEnum.entries.size
    }

    override fun getItem(position: Int): SortingVariantsEnum? {
        return if (position in 0 until SortingVariantsEnum.entries.size) {
            SortingVariantsEnum.entries[position]
        } else {
            null
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}