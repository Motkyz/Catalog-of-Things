package com.example.catalogofthings.di.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
): ViewModelProvider.Factory {

    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        val viewModelProvider = viewModels[modelClass] ?:
        throw IllegalStateException("Забыл про $modelClass")

        return viewModelProvider.get() as T
    }
}