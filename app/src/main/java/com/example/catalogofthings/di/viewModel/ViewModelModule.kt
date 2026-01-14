package com.example.catalogofthings.di.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.catalogofthings.presenter.MainViewModel
import com.example.catalogofthings.presenter.viewModels.ChooseFolderBottomSheetViewModel
import com.example.catalogofthings.presenter.viewModels.ChooseTagsBottomSheetViewModel
import com.example.catalogofthings.presenter.viewModels.FolderViewModel
import com.example.catalogofthings.presenter.viewModels.TagViewModel
import com.example.catalogofthings.presenter.viewModels.NoteFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    fun bindViewModelFactory(
        factory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    fun bindMainViewModel(
        viewModel: MainViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FolderViewModel::class)
    fun bindFolderViewModel(
        viewModel: FolderViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NoteFragmentViewModel::class)
    fun bindNoteFragmentViewModel(
        viewModel: NoteFragmentViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TagViewModel::class)
    fun bindTagViewModel(
        viewModel: TagViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChooseTagsBottomSheetViewModel::class)
    fun bindChooseTagsBottomSheetViewModel(
        viewModel: ChooseTagsBottomSheetViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChooseFolderBottomSheetViewModel::class)
    fun bindChooseFolderBottomSheetViewModel(
        viewModel: ChooseFolderBottomSheetViewModel
    ): ViewModel
}