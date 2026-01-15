package com.example.catalogofthings.presenter.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogofthings.data.model.TagEntity
import com.example.catalogofthings.domain.tagsUseCases.CreateTagUseCase
import com.example.catalogofthings.domain.tagsUseCases.GetTagUseCase
import com.example.catalogofthings.domain.tagsUseCases.UpdateTagUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class TagViewModel @Inject constructor(
    private val createTagUseCase: CreateTagUseCase,
    private val updateTagUseCase: UpdateTagUseCase,
    private val getTagUseCase: GetTagUseCase
): ViewModel() {

    private var _color = MutableLiveData<Int?>()
    val color: LiveData<Int?>
        get() = _color

    private var _tag = MutableLiveData<TagEntity?>()
    val tag: LiveData<TagEntity?>
        get() = _tag

    fun getTag(id : Int) {
        viewModelScope.launch {
            _tag.postValue(getTagUseCase(id))
        }
    }

    fun createTag(title: String) {
        viewModelScope.launch {
            createTagUseCase(TagEntity (
                title = title,
                color = color.value ?: -1
            ))
        }
    }

    fun updateTag(oldTag : TagEntity, newTag : TagEntity) {
        viewModelScope.launch {
            updateTagUseCase(oldTag, newTag)
        }
    }

    fun saveColor(color: Int) {
        viewModelScope.launch {
            _color.postValue( color)
        }
    }
}