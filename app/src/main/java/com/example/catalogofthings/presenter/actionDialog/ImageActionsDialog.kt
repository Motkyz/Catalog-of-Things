package com.example.catalogofthings.presenter.actionDialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.catalogofthings.data.model.ImageEntity

object ImageActionsDialog {
    fun show(
        context: Context,
        image: ImageEntity,
        onDelete: () -> Unit,
    ) {
        val items = arrayOf("Удалить")


        AlertDialog.Builder(context)
            .setTitle(image.imageId.toString())
            .setItems(items) { _, which ->
                when (which) {
                    0 -> onDelete()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}