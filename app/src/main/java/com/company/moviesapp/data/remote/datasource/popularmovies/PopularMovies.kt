package com.company.moviesapp.data.remote.datasource.popularmovies

import com.company.moviesapp.data.remote.dto.MovieListResponse

interface PopularMovies {
    suspend fun getMovies(pageNumber: Int): MovieListResponse
}