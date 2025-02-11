package com.company.moviesapp.data.remote.datasource

import com.company.moviesapp.data.remote.dto.MovieListResponse

interface MovieListRemoteDataSource {
    suspend fun getMovies(pageNumber: Int): MovieListResponse
}