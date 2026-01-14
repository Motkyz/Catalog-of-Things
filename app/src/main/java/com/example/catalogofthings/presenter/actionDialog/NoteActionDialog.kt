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


        AlertDialog.Builder(context)
            .setTitle("Выберите действие с " + note.title)
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