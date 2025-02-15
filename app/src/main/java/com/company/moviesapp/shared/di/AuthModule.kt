package com.company.moviesapp.shared.di

import com.company.moviesapp.shared.data.local.datasource.TokenProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideTokenProvider(): TokenProvider {
        return TokenProvider()
    }
}