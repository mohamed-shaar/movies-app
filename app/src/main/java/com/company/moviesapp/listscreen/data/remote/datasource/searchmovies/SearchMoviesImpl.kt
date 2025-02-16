package com.company.moviesapp.listscreen.data.remote.datasource.searchmovies

import com.company.moviesapp.listscreen.data.remote.dto.SearchMoviesResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

class SearchMoviesImpl(private val client: HttpClient): SearchMoviesRemoteDataSource {
    override suspend fun searchMovies(query: String, pageNumber: Int): SearchMoviesResponse {
        return client.get {
            url("https://api.themoviedb.org/3/search/movie")
            parameter(key = "page", value = pageNumber)
            parameter(key = "query", value = query)
        }
    }
}