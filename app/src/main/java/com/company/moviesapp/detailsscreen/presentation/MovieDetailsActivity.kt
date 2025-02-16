package com.company.moviesapp.detailsscreen.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.company.moviesapp.detailsscreen.presentation.model.CastDisplayModel
import com.company.moviesapp.detailsscreen.presentation.model.MovieDetailsDisplayModel
import com.company.moviesapp.detailsscreen.presentation.viewmodel.MovieDetailsUiState
import com.company.moviesapp.detailsscreen.presentation.viewmodel.MovieDetailsViewModel
import com.company.moviesapp.shared.presentation.ui.ImageWithPlaceholder
import com.company.moviesapp.shared.presentation.ui.MovieItem
import com.company.moviesapp.shared.presentation.ui.WatchLaterIcon
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsActivity : ComponentActivity() {
    private lateinit var movieId: String

    private val movieDetailsViewModel: MovieDetailsViewModel by viewModels<MovieDetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieId = intent.getStringExtra("id") ?: ""
        movieDetailsViewModel.getData(movieId)
        setContent {
            Scaffold { innerPadding ->
                DetailsScreen(movieDetailsViewModel, innerPadding, onRetry = {
                    movieDetailsViewModel.getData(movieId)
                })
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailsScreen(
    movieViewModel: MovieDetailsViewModel,
    innerPadding: PaddingValues,
    onRetry: () -> Unit
) {
    val moviesState by movieViewModel.moviesState.collectAsStateWithLifecycle()

    when (moviesState) {
        is MovieDetailsUiState.Loading -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }

        is MovieDetailsUiState.Success -> {
            val movieDetails: MovieDetailsDisplayModel =
                (moviesState as MovieDetailsUiState.Success).movieDetailsDisplayModel
            val pagerState =
                rememberPagerState(pageCount = { movieDetails.similarMovies.size }) // State to track the current page
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ImageWithPlaceholder(
                        movieDetails.image,
                        Modifier
                            .padding(8.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = movieDetails.title)
                            WatchLaterIcon(
                                isAddedToWatchLater = movieDetails.addToWatch,
                                onToggleWatchLater = {
                                    movieViewModel.toggleWatchLater()

                                })
                        }
                        Text(text = movieDetails.tagline)
                        Text(text = movieDetails.overview)
                        Text(text = movieDetails.status)
                        Text(text = movieDetails.revenue.toString())
                        Text(text = movieDetails.releaseDate)
                    }
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text(text = "Similar Movies", modifier = Modifier.padding(horizontal = 8.dp))
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .fillMaxHeight(0.3f)
                        .padding(horizontal = 8.dp)
                ) { page ->
                    // Content for each page
                    MovieItem(
                        movie = movieDetails.similarMovies[page],
                        onClick = {},
                        onToggleWatchLater = {})
                }
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text(text = "Actors")
                LazyRow {
                    items(movieDetails.cast) { actor ->
                        ProfileItem(actor)
                    }
                }
                Box(modifier = Modifier.height(8.dp))
                Text(text = "Directors")
                LazyRow {
                    items(movieDetails.crew) { director ->
                        ProfileItem(director)
                    }
                }
                Box(modifier = Modifier.height(16.dp))
            }
        }

        is MovieDetailsUiState.Error -> {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "An unexpected error happened. Please try again.",
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Button(onClick = { onRetry() }) {
                    Text(text = "Retry")
                }
            }
        }
    }
}

@Composable
private fun ProfileItem(actor: CastDisplayModel) {
    Column(modifier = Modifier.padding(end = 16.dp)) {
        Box(modifier = Modifier.clip(RoundedCornerShape(8.dp))) {
            ImageWithPlaceholder(imageUrl = actor.profilePath)
        }
        Text(text = actor.name)
    }
}