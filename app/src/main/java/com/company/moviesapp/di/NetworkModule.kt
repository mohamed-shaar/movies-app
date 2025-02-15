package com.company.moviesapp.di

import com.company.moviesapp.data.remote.datasource.popularmovies.PopularMoviesImpl
import com.company.moviesapp.data.remote.datasource.popularmovies.PopularMoviesRemoteDataSource
import com.company.moviesapp.data.remote.datasource.searchmovies.SearchMoviesImpl
import com.company.moviesapp.data.remote.datasource.searchmovies.SearchMoviesRemoteDataSource
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
    fun provideHttpClient(): HttpClient {
        return HttpClient {
            install(Logging) {
                level = LogLevel.ALL
            }
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
            defaultRequest {
                headers {
                    append(
                        HttpHeaders.Authorization,
                        "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmMzRjNDUyNzk3ZTJkNDk3ZmFlNjE3OWMxNjVjNGY0YSIsIm5iZiI6MTU2MzA5NDczNi44MDQ5OTk4LCJzdWIiOiI1ZDJhZWVkMGEyOTRmMDI4NDYyZTc3MzEiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.U74vUrPid2qhLBWbpe9j1W_ScNl9nEAEktulzeZHB8o"
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
}