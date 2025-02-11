package com.company.moviesapp.data.remote.datasource

import com.company.moviesapp.data.remote.dto.MovieListResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

class MovieListImpl(private val client: HttpClient): MovieListRemoteDataSource {
    override suspend fun getMovies(pageNumber: Int): MovieListResponse {
            return client.get {
                url("https://api.themoviedb.org/3/movie/popular")
                parameter(key = "page", value = pageNumber)
            }
    }
}