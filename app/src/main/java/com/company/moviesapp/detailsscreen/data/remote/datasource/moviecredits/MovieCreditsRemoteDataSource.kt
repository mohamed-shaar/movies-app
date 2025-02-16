package com.company.moviesapp.detailsscreen.data.remote.datasource.moviecredits

import com.company.moviesapp.detailsscreen.data.remote.dto.MovieCreditsResponse

interface MovieCreditsRemoteDataSource {
    suspend fun getMovieCredits(id: String): MovieCreditsResponse
}