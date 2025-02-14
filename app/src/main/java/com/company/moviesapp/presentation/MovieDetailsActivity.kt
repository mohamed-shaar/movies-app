package com.company.moviesapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage
import com.company.moviesapp.data.remote.datasource.moviecredits.MovieCreditsImpl
import com.company.moviesapp.data.remote.datasource.moviecredits.MovieCreditsRemoteDataSource
import com.company.moviesapp.data.remote.datasource.moviedetails.MovieDetailsImpl
import com.company.moviesapp.data.remote.datasource.moviedetails.MovieDetailsRemoteDataSource
import com.company.moviesapp.data.remote.datasource.similarmovies.SimilarMoviesImpl
import com.company.moviesapp.data.remote.datasource.similarmovies.SimilarMoviesRemoteDataSource
import com.company.moviesapp.presentation.mappers.MovieDetailsMapperImpl
import com.company.moviesapp.presentation.models.MovieDetailsDisplayModel
import com.company.moviesapp.presentation.viewmodel.MovieDetailsUiState
import com.company.moviesapp.presentation.viewmodel.MovieDetailsViewModel
import com.company.moviesapp.presentation.viewmodel.MovieDetailsViewModelFactory
import io.ktor.client.HttpClient
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.runBlocking

class MovieDetailsActivity : ComponentActivity() {

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

    private val movieDetailsRemoteDataSource: MovieDetailsRemoteDataSource =
        MovieDetailsImpl(client)
    private val movieCreditsRemoteDataSource: MovieCreditsRemoteDataSource =
        MovieCreditsImpl(client)
    private val similarMoviesRemoteDataSource: SimilarMoviesRemoteDataSource =
        SimilarMoviesImpl(client)

    private val movieDetailsViewModel: MovieDetailsViewModel by viewModels<MovieDetailsViewModel> {
        MovieDetailsViewModelFactory(
            movieDetailsRemoteDataSource,
            movieCreditsRemoteDataSource,
            similarMoviesRemoteDataSource,
            MovieDetailsMapperImpl(),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        runBlocking {
            movieDetailsViewModel.getMovieDetailsScreen(939243)
        }
        setContent {
            Scaffold { innerPadding ->
                DetailsScreen(movieDetailsViewModel, innerPadding)
            }
        }
    }
}

@Composable
fun DetailsScreen(movieViewModel: MovieDetailsViewModel, innerPadding: PaddingValues) {
    val moviesState by movieViewModel.moviesState.collectAsState()

    when (moviesState) {
        is MovieDetailsUiState.Loading -> {
            CircularProgressIndicator()
        }

        is MovieDetailsUiState.Success -> {
            val movieDetails: MovieDetailsDisplayModel =
                (moviesState as MovieDetailsUiState.Success).movieDetailsDisplayModel
            Column(modifier = Modifier.padding(innerPadding)) {
                Row {
                    AsyncImage(model = movieDetails.image, contentDescription = null)
                    Column {
                        Text(text = movieDetails.title)
                        Text(text = movieDetails.tagline)
                        Text(text = movieDetails.overview)
                        Text(text = movieDetails.status)
                    }
                }

            }
        }

        is MovieDetailsUiState.Error -> {
            Button(onClick = { }) {
                Text(text = "Retry")
            }
        }
    }
}