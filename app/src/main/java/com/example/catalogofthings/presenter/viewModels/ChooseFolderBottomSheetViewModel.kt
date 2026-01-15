package com.example.catalogofthings.presenter.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogofthings.data.model.NoteEntity
import com.example.catalogofthings.data.model.TagEntity
import com.example.catalogofthings.domain.notesUseCases.GetAllFoldersUseCase
import com.example.catalogofthings.domain.notesUseCases.UpdateNoteUseCase
import com.example.catalogofthings.domain.tagsUseCases.DeleteTagUseCase
import com.example.catalogofthings.domain.tagsUseCases.GetTagsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChooseFolderBottomSheetViewModel @Inject constructor(
    private val getAllFoldersUseCase: GetAllFoldersUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase
): ViewModel() {

    private val _folders = MutableLiveData<List<NoteEntity>>()
    val folders: LiveData<List<NoteEntity>>
        get() = _folders

    fun getFolders(id : Int){
        viewModelScope.launch {
            getAllFoldersUseCase(id).collect {
                _folders.postValue(it)
            }
        }
    }


    fun updateFolder(oldFolder: NoteEntity, newFolder: NoteEntity) {
        viewModelScope.launch {
            updateNoteUseCase(oldFolder, newFolder)
        }
    }
}