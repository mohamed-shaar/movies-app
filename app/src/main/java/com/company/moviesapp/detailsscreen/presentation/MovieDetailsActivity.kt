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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                rememberPagerState(pageCount = { movieDetails.similarMovies.size })

            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Movie Poster and Details
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Movie Poster
                    ImageWithPlaceholder(
                        imageUrl = movieDetails.image,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .height(200.dp)
                            .weight(0.4f)
                    )

                    // Movie Details
                    Column(
                        modifier = Modifier
                            .weight(0.6f)
                            .padding(start = 16.dp)
                    ) {
                        // Title and Watch Later Icon
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = movieDetails.title,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                            WatchLaterIcon(
                                isAddedToWatchLater = movieDetails.addToWatch,
                                onToggleWatchLater = { movieViewModel.toggleWatchLater() }
                            )
                        }

                        // Tagline
                        Text(
                            text = movieDetails.tagline,
                            fontSize = 16.sp,
                            fontStyle = FontStyle.Italic,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        // Overview
                        Text(
                            text = movieDetails.overview,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 16.dp)
                        )

                        // Additional Details
                        Text(
                            text = "Status: ${movieDetails.status}",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = "Revenue: $${movieDetails.revenue}",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = "Release Date: ${movieDetails.releaseDate}",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                // Divider
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )

                // Similar Movies Section
                Text(
                    text = "Similar Movies",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(vertical = 8.dp)
                ) { page ->
                    MovieItem(
                        movie = movieDetails.similarMovies[page],
                        onClick = {},
                        onToggleWatchLater = {}
                    )
                }

                // Divider
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )

                // Cast Section
                Text(
                    text = "Cast",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(movieDetails.cast) { actor ->
                        ProfileItem(actor)
                    }
                }

                // Divider
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 16.dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )

                // Directors Section
                Text(
                    text = "Directors",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(movieDetails.crew) { director ->
                        ProfileItem(director)
                    }
                }

                // Bottom Spacing
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
                    fontSize = 16.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Button(
                    onClick = { onRetry() },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Blue,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Retry", fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
private fun ProfileItem(actor: CastDisplayModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(120.dp)
            .padding(8.dp)
    ) {
        // Actor Image
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .size(100.dp)
        ) {
            ImageWithPlaceholder(
                imageUrl = actor.profilePath,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Actor Name
        Text(
            text = actor.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}