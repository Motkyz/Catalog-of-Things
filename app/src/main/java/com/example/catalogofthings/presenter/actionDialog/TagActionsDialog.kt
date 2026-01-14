package com.example.catalogofthings.presenter.actionDialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.catalogofthings.data.model.ImageEntity
import com.example.catalogofthings.data.model.TagEntity

object TagActionsDialog {

    fun show(
        context: Context,
        tag: TagEntity,
        onDelete: () -> Unit,
        onEdit: () -> Unit
    ) {
        val items = arrayOf("Редактировать", "Удалить")


        AlertDialog.Builder(context)
            .setTitle("Выберите действие с " + tag.title)
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