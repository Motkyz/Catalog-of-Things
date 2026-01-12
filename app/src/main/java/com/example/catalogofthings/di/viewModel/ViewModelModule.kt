package com.example.catalogofthings.di.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.catalogofthings.presenter.MainViewModel
import com.example.catalogofthings.presenter.viewModels.NewTagViewModel
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
    @ViewModelKey(NewTagViewModel::class)
    fun bindNewTagViewModel(
        viewModel: NewTagViewModel
    ): ViewModel
}