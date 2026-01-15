package com.example.catalogofthings.presenter.actionDialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.catalogofthings.R
import com.example.catalogofthings.data.model.ImageEntity

object ImageActionsDialog {
    fun show(
        context: Context,
        onAccept: () -> Unit,
    ) {
        AlertDialog.Builder(context, com.google.android.material.R.style.ThemeOverlay_Material3_Dialog)
            .setTitle("Удалить изображение?")
            .setPositiveButton("Удалить"){
                dialog, _ ->
                    onAccept()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}