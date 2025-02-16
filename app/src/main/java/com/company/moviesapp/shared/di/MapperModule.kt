package com.company.moviesapp.shared.di

import com.company.moviesapp.detailsscreen.presentation.mapper.MovieDetailsMapper
import com.company.moviesapp.detailsscreen.presentation.mapper.MovieDetailsMapperImpl
import com.company.moviesapp.listscreen.presentation.mapper.MovieMapper
import com.company.moviesapp.listscreen.presentation.mapper.MovieMapperImpl
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