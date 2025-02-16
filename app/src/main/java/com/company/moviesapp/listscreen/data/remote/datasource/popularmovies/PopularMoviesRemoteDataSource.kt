package com.company.moviesapp.listscreen.data.remote.datasource.popularmovies

import com.company.moviesapp.listscreen.data.remote.dto.PopularMoviesResponse

interface PopularMoviesRemoteDataSource {
    suspend fun getMovies(pageNumber: Int): PopularMoviesResponse
}