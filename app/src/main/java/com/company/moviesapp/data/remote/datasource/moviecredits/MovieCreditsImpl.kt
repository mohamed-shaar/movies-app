package com.company.moviesapp.data.remote.datasource.moviecredits

import com.company.moviesapp.data.remote.dto.MovieCreditsResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url

class MovieCreditsImpl(private val client: HttpClient) : MovieCreditsRemoteDataSource {
    override suspend fun getMovieCredits(id: String): MovieCreditsResponse {
        return client.get {
            url("https://api.themoviedb.org/3/movie/$id/credits")
        }
    }
}