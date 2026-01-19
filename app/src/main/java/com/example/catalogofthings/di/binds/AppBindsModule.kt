package com.example.catalogofthings.di.binds

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.catalogofthings.data.db.NotesDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
interface AppBindsModule {

    companion object {
        @Provides
        fun provideContext(app: Application): Context =
            app.applicationContext

        @Provides
        @Singleton
        fun provideDb(context: Context): NotesDatabase =
            Room.databaseBuilder(
                context,
                NotesDatabase::class.java,
                "notes.db"
            ).build()
    }
}