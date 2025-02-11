package com.company.moviesapp.data.remote.datasource.similarmovies

import com.company.moviesapp.data.remote.dto.SimilarMoviesResponse

interface SimilarMoviesRemoteDataSource {
    suspend fun getSimilarMovies(id: String): SimilarMoviesResponse
}