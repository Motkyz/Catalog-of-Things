package com.example.catalogofthings.di

import android.app.Fragment
import com.example.catalogofthings.di.viewModel.ViewModelModule
import com.example.catalogofthings.presenter.MainFragment
import dagger.Component
import dagger.Module

@Component(
    modules = [AppModule::class]
)
abstract class AppComponent {
    abstract fun inject(fragment: MainFragment)
}

@Module(
    includes = [
        AppBindsModule::class,
        ViewModelModule::class
    ]
)
class AppModule