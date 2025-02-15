package com.company.moviesapp.shared.di

import com.company.moviesapp.presentation.mappers.MovieDetailsMapper
import com.company.moviesapp.presentation.mappers.MovieDetailsMapperImpl
import com.company.moviesapp.presentation.mappers.MovieMapper
import com.company.moviesapp.presentation.mappers.MovieMapperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {

    @Provides
    @Singleton
    fun provideMovieMapper(): MovieMapper {
        return MovieMapperImpl()
    }

    @Provides
    @Singleton
    fun provideMovieDetailsMapper(): MovieDetailsMapper {
        return MovieDetailsMapperImpl()
    }

}