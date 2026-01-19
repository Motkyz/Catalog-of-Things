package com.example.catalogofthings.di

import android.app.Application
import com.example.catalogofthings.di.binds.AppBindsModule
import com.example.catalogofthings.di.binds.ImagesBindsModule
import com.example.catalogofthings.di.binds.NotesBindsModule
import com.example.catalogofthings.di.binds.TagsBindsModule
import com.example.catalogofthings.di.viewModel.ViewModelModule
import com.example.catalogofthings.presenter.fragments.BaseFolderFragment
import com.example.catalogofthings.presenter.fragments.ChooseFolderBottomSheet
import com.example.catalogofthings.presenter.fragments.ChooseTagsBottomSheet
import com.example.catalogofthings.presenter.fragments.CreateFolderBottomSheet
import com.example.catalogofthings.presenter.fragments.FolderFragment
import com.example.catalogofthings.presenter.fragments.TagFragment
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
    abstract fun inject(fragment: TagFragment)
    abstract fun inject(fragment: ChooseTagsBottomSheet)
    abstract fun inject(fragment: ChooseFolderBottomSheet)
    abstract fun inject(fragment: CreateFolderBottomSheet)

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
        NotesBindsModule::class,
        TagsBindsModule::class,
        ImagesBindsModule::class,
        ViewModelModule::class
    ]
)
class AppModule