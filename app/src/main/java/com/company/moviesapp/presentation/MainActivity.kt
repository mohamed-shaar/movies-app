package com.company.moviesapp.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.company.moviesapp.data.remote.datasource.popularmovies.PopularMoviesImpl
import com.company.moviesapp.data.remote.datasource.popularmovies.PopularMoviesRemoteDataSource
import com.company.moviesapp.data.remote.datasource.searchmovies.SearchMoviesImpl
import com.company.moviesapp.data.remote.datasource.searchmovies.SearchMoviesRemoteDataSource
import com.company.moviesapp.presentation.ui.MovieList
import com.company.moviesapp.presentation.viewmodel.MoviesViewModel
import com.company.moviesapp.presentation.viewmodel.MoviesViewModelFactory
import com.company.moviesapp.ui.theme.MoviesAppTheme
import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders

class MainActivity : ComponentActivity() {

    private val client = HttpClient {
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

    private val movieService: PopularMoviesRemoteDataSource = PopularMoviesImpl(client)
    private val searchMoviesService: SearchMoviesRemoteDataSource = SearchMoviesImpl(client)

    private val moviesViewModel by viewModels<MoviesViewModel> {
        MoviesViewModelFactory(
            movieService,
            searchMoviesService
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        moviesViewModel.getData()
        setContent {
            MoviesAppTheme {
                Scaffold(modifier = Modifier.fillMaxWidth()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        MovieList(
                            moviesViewModel = moviesViewModel,
                            onClick = { id -> // Create an Intent to navigate to SecondActivity
                                val intent =
                                    Intent(applicationContext, MovieDetailsActivity::class.java)

                                // Optionally, pass data to the next activity
                                intent.putExtra("id", id)

                                // Start the SecondActivity
                                startActivity(intent)
                            }, onSearch = { query ->
                                searchMovies(query)
                            })
                    }
                }
            }
        }
    }

    private fun searchMovies(it: String) {
        moviesViewModel.setSearchText(it)
    }
}