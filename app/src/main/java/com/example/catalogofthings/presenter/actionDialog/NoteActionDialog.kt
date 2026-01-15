package com.example.catalogofthings.presenter.actionDialog

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.catalogofthings.data.model.NoteEntity

object NoteActionDialog {

    fun show(
        context: Context,
        note: NoteEntity,
        onDelete: () -> Unit = {},
        replaceInFolder: () -> Unit = {}
    ) {
        val items = arrayOf("Перенести","Удалить")
        val title =
            if(note.isFolder) "Выберите действие с папкой \"${note.title}\""
            else "Выберите действие с заметкой \"${note.title}\""


        AlertDialog.Builder(context, com.google.android.material.R.style.ThemeOverlay_Material3_Dialog)
            .setTitle(title)
            .setItems(items) { _, which ->
                when (which) {
                    0 -> replaceInFolder()
                    1 -> onDelete()
                }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

}