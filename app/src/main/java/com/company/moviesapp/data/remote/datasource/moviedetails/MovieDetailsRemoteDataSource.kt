package com.company.moviesapp.data.remote.datasource.moviedetails

import com.company.moviesapp.data.remote.dto.MovieDetailsResponse

interface MovieDetailsRemoteDataSource {
    suspend fun getMovieDetails(id: String): MovieDetailsResponse
}