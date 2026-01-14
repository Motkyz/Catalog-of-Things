package com.example.catalogofthings.presenter.fragments

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.catalogofthings.data.model.TagEntity

object TagActionsDialog {

    fun show(
        context: Context,
        tag: TagEntity,
        onDelete: () -> Unit,
        onEdit: (() -> Unit)
    ) {
        val items = arrayOf("Удалить", "Редактировать")


        AlertDialog.Builder(context)
            .setTitle(tag.title)
            .setItems(items) { _, which ->
                when (which) {
                    0 -> onDelete()
                    1 -> onEdit.invoke()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}