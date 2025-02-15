package com.company.moviesapp.shared.di

import android.content.Context
import com.company.moviesapp.data.local.datasource.WatchLaterLocalDataSource
import com.company.moviesapp.data.local.datasource.WatchLaterLocalDatasourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Scope the dependency to the application lifecycle
object AppModule {

    @Provides
    @Singleton
    fun provideWatchLaterHelper(@ApplicationContext context: Context): WatchLaterLocalDataSource {
        return WatchLaterLocalDatasourceImpl(
            context.getSharedPreferences(
                "watch_later",
                Context.MODE_PRIVATE
            )
        )
    }
}