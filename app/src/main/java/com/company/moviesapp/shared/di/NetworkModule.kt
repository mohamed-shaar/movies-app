package com.company.moviesapp.shared.di

import com.company.moviesapp.detailsscreen.data.remote.datasource.moviecredits.MovieCreditsImpl
import com.company.moviesapp.detailsscreen.data.remote.datasource.moviecredits.MovieCreditsRemoteDataSource
import com.company.moviesapp.detailsscreen.data.remote.datasource.moviedetails.MovieDetailsImpl
import com.company.moviesapp.detailsscreen.data.remote.datasource.moviedetails.MovieDetailsRemoteDataSource
import com.company.moviesapp.detailsscreen.data.remote.datasource.similarmovies.SimilarMoviesImpl
import com.company.moviesapp.detailsscreen.data.remote.datasource.similarmovies.SimilarMoviesRemoteDataSource
import com.company.moviesapp.listscreen.data.remote.datasource.popularmovies.PopularMoviesImpl
import com.company.moviesapp.listscreen.data.remote.datasource.popularmovies.PopularMoviesRemoteDataSource
import com.company.moviesapp.listscreen.data.remote.datasource.searchmovies.SearchMoviesImpl
import com.company.moviesapp.listscreen.data.remote.datasource.searchmovies.SearchMoviesRemoteDataSource
import com.company.moviesapp.shared.data.local.datasource.TokenProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class) // Scope the dependencies to the application lifecycle
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(tokenProvider: TokenProvider): HttpClient {
        return HttpClient {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
            defaultRequest {
                val token = tokenProvider.getToken()
                headers {
                    append(
                        HttpHeaders.Authorization,
                        "Bearer $token"
                    )
                }
            }
        }
    }

    @Provides
    @Singleton
    fun providePopularMovieDetailsRemoteDataSource(client: HttpClient): PopularMoviesRemoteDataSource {
        return PopularMoviesImpl(client) // Provide the implementation
    }

    @Provides
    @Singleton
    fun provideSearchMovieDetailsRemoteDataSource(client: HttpClient): SearchMoviesRemoteDataSource {
        return SearchMoviesImpl(client) // Provide the implementation
    }

    @Provides
    @Singleton
    fun provideMovieDetailsRemoteDataSource(client: HttpClient): MovieDetailsRemoteDataSource {
        return MovieDetailsImpl(client) // Provide the implementation
    }

    @Provides
    @Singleton
    fun provideMovieCreditsRemoteDataSource(client: HttpClient): MovieCreditsRemoteDataSource {
        return MovieCreditsImpl(client) // Provide the implementation
    }

    @Provides
    @Singleton
    fun provideSimilarMoviesRemoteDataSource(client: HttpClient): SimilarMoviesRemoteDataSource {
        return SimilarMoviesImpl(client) // Provide the implementation
    }
}