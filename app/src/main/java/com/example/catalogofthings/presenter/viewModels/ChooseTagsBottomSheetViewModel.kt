package com.example.catalogofthings.presenter.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogofthings.data.model.TagEntity
import com.example.catalogofthings.domain.tagsUseCases.DeleteTagUseCase
import com.example.catalogofthings.domain.tagsUseCases.GetTagsUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChooseTagsBottomSheetViewModel @Inject constructor(
    private val getTagsUseCase: GetTagsUseCase,
    private val deleteTagUseCase: DeleteTagUseCase
): ViewModel() {

    init {
        getTags()
    }

    private val _tags = MutableLiveData<List<TagEntity>>()
    val tags: LiveData<List<TagEntity>>
        get() = _tags

    fun getTags() {
        viewModelScope.launch {
            getTagsUseCase().collect {
                _tags.postValue(it)
            }
        }
    }

    fun deleteTag(tag : TagEntity) {
        viewModelScope.launch {
            deleteTagUseCase(tag)
        }
    }
}