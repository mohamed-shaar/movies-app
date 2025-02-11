package com.company.moviesapp.data.remote.datasource.popularmovies

import com.company.moviesapp.data.remote.dto.PopularMoviesResponse

interface PopularMoviesRemoteDataSource {
    suspend fun getMovies(pageNumber: Int): PopularMoviesResponse
}