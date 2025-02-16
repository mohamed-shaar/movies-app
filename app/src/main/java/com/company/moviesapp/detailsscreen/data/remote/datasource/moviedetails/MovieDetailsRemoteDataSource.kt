package com.company.moviesapp.detailsscreen.data.remote.datasource.moviedetails

import com.company.moviesapp.detailsscreen.data.remote.dto.MovieDetailsResponse

interface MovieDetailsRemoteDataSource {
    suspend fun getMovieDetails(id: String): MovieDetailsResponse
}