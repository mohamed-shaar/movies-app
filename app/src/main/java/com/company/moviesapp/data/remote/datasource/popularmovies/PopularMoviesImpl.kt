package com.company.moviesapp.data.remote.datasource.popularmovies

import com.company.moviesapp.data.remote.dto.PopularMoviesResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

class PopularMoviesImpl(private val client: HttpClient): PopularMovies {
    override suspend fun getMovies(pageNumber: Int): PopularMoviesResponse {
            return client.get {
                url("https://api.themoviedb.org/3/movie/popular")
                parameter(key = "page", value = pageNumber)
            }
    }
}