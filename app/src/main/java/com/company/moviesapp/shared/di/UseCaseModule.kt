package com.company.moviesapp.shared.di

import com.company.moviesapp.detailsscreen.data.remote.datasource.moviecredits.MovieCreditsRemoteDataSource
import com.company.moviesapp.detailsscreen.data.remote.datasource.moviedetails.MovieDetailsRemoteDataSource
import com.company.moviesapp.detailsscreen.data.remote.datasource.similarmovies.SimilarMoviesRemoteDataSource
import com.company.moviesapp.presentation.mappers.MovieDetailsMapper
import com.company.moviesapp.presentation.usecase.GetMovieDetailsScreenUseCase
import com.company.moviesapp.presentation.usecase.GetMovieDetailsScreenUseCaseImpl
import com.company.moviesapp.shared.data.local.datasource.WatchLaterLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Scope the dependencies to the application lifecycle
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetMovieDetailsScreenUseCase(
        movieDetailsRemoteDataSource: MovieDetailsRemoteDataSource,
        movieCreditsRemoteDataSource: MovieCreditsRemoteDataSource,
        similarMoviesRemoteDataSource: SimilarMoviesRemoteDataSource,
        movieDetailsMapper: MovieDetailsMapper,
        watchLaterLocalDataSource: WatchLaterLocalDataSource
    ): GetMovieDetailsScreenUseCase {
        return GetMovieDetailsScreenUseCaseImpl(
            movieDetailsRemoteDataSource,
            movieCreditsRemoteDataSource,
            similarMoviesRemoteDataSource,
            movieDetailsMapper,
            watchLaterLocalDataSource
        )
    }
}