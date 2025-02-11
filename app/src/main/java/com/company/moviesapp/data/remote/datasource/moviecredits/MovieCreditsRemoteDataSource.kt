package com.company.moviesapp.data.remote.datasource.moviecredits

import com.company.moviesapp.data.remote.dto.MovieCreditsResponse

interface MovieCreditsRemoteDataSource {
    suspend fun getMovieCredits(id: String): MovieCreditsResponse
}