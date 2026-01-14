package com.example.catalogofthings.presenter.actionDialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.catalogofthings.data.model.ImageEntity

object ImageActionsDialog {
    fun show(
        context: Context,
        onAccept: () -> Unit,
    ) {
        AlertDialog.Builder(context)
            .setTitle("Удалить изображение?")
            .setPositiveButton("Удалить"){
                dialog, _ ->
                    onAccept()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}