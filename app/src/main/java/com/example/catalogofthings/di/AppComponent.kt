package com.example.catalogofthings.di

import android.app.Application
import com.example.catalogofthings.di.viewModel.ViewModelModule
import com.example.catalogofthings.presenter.fragments.ChooseTagsBottomSheet
import com.example.catalogofthings.presenter.fragments.FolderFragment
import com.example.catalogofthings.presenter.fragments.NewTagFragment
import com.example.catalogofthings.presenter.fragments.NoteFragment
import com.example.catalogofthings.presenter.fragments.StartFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Component(
    modules = [AppModule::class]
)
@Singleton
abstract class AppComponent {
    abstract fun inject(fragment: StartFragment)
    abstract fun inject(fragment: FolderFragment)
    abstract fun inject(fragment: NoteFragment)
    abstract fun inject(fragment: NewTagFragment)
    abstract fun inject(fragment: ChooseTagsBottomSheet)


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder
        fun build(): AppComponent
    }
}

@Module(
    includes = [
        AppBindsModule::class,
        ViewModelModule::class
    ]
)
class AppModule