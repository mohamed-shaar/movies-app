package com.company.moviesapp.data.remote.datasource.moviedetails

import com.company.moviesapp.data.remote.dto.MovieDetails
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.url

class MovieDetailsImpl(private val client: HttpClient) : MovieDetailsRemoteDataSource {
    override suspend fun getMovieDetails(id: String): MovieDetails {
        return client.get {
            url("https://api.themoviedb.org/3/movie/939243")
        }
    }
}