package com.company.moviesapp.data.remote.datasource.searchmovies

import com.company.moviesapp.data.remote.dto.SearchMoviesResponse

interface SearchMoviesRemoteDataSource {
    suspend fun searchMovies( query: String,  pageNumber: Int): SearchMoviesResponse
}