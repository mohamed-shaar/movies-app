package com.company.moviesapp.detailsscreen.data.remote.datasource.similarmovies

import com.company.moviesapp.detailsscreen.data.remote.dto.SimilarMoviesResponse

interface SimilarMoviesRemoteDataSource {
    suspend fun getSimilarMovies(id: String): SimilarMoviesResponse
}