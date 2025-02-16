package com.company.moviesapp.detailsscreen.data.remote.datasource.similarmovies

import com.company.moviesapp.detailsscreen.data.remote.dto.SimilarMoviesResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url

class SimilarMoviesImpl(private val client: HttpClient) : SimilarMoviesRemoteDataSource {
    override suspend fun getSimilarMovies(id: String): SimilarMoviesResponse {
        return client.get {
            url("https://api.themoviedb.org/3/movie/$id/similar")
        }
    }
}