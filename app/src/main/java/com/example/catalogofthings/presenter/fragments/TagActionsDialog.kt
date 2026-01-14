package com.example.catalogofthings.presenter.fragments

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.catalogofthings.data.model.TagEntity

object TagActionsDialog {

    fun show(
        context: Context,
        tag: TagEntity,
        onDelete: () -> Unit,
        onEdit: (() -> Unit)? = null
    ) {
        val items = mutableListOf(("Удалить"))

        AlertDialog.Builder(context)
            .setTitle(tag.title)
            .setItems(items.toTypedArray()) { _, which ->
                when (which) {
                    0 -> onDelete()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}