package com.company.moviesapp.listscreen.data.remote.datasource.searchmovies

import com.company.moviesapp.listscreen.data.remote.dto.SearchMoviesResponse

interface SearchMoviesRemoteDataSource {
    suspend fun searchMovies( query: String,  pageNumber: Int): SearchMoviesResponse
}