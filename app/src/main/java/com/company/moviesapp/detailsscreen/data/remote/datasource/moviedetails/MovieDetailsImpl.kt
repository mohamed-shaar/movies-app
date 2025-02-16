package com.company.moviesapp.detailsscreen.data.remote.datasource.moviedetails

import com.company.moviesapp.detailsscreen.data.remote.dto.MovieDetailsResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url

class MovieDetailsImpl(private val client: HttpClient) : MovieDetailsRemoteDataSource {
    override suspend fun getMovieDetails(id: String): MovieDetailsResponse {
        return client.get {
            url("https://api.themoviedb.org/3/movie/$id")
        }
    }
}