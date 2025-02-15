package com.company.moviesapp.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.company.moviesapp.presentation.models.MovieDisplayModel
import com.company.moviesapp.presentation.viewmodel.MovieUiState
import com.company.moviesapp.presentation.viewmodel.MoviesViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieList(
    moviesViewModel: MoviesViewModel,
    onClick: (String) -> Unit,
    onSearch: (String) -> Unit
) {

    val moviesState by moviesViewModel.moviesState.collectAsStateWithLifecycle()

    when (moviesState) {
        is MovieUiState.Loading -> {
            return Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }

        is MovieUiState.Success -> {
            val groupedMovies = (moviesState as MovieUiState.Success).movies
            SimpleOutlinedTextFieldSample(
                onCall = { input ->
                    onSearch(
                        input,
                    )
                }
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                groupedMovies.forEach { group ->
                    // Sticky header for the year
                    stickyHeader {
                        YearHeader(group.year)
                    }

                    // List of movies in this group
                    items(group.movies) { movie ->
                        MovieItem(movie, onClick)
                    }
                }
            }
        }

        else -> {
            return Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = "An unexpected error happened. Please try again.",
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Button(onClick = { moviesViewModel.getData() }) {
                    Text("Retry")
                }
            }
        }
    }


}

@Composable
fun MovieItem(movie: MovieDisplayModel, onClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable {
                onClick(movie.id)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row {
            ImageWithPlaceholder(
                imageUrl = movie.image,
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = movie.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movie.overview,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (movie.addToWatch) "Added to Watchlist" else "Not in Watchlist",
                    fontSize = 12.sp,
                    color = if (movie.addToWatch) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun SimpleOutlinedTextFieldSample(onCall: (input: String) -> Unit) {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it; onCall(it) },
        label = { Text("Search For Movie") },
        shape = OutlinedTextFieldDefaults.shape,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}

@Composable
fun ImageWithPlaceholder(
    imageUrl: String, modifier: Modifier = Modifier
) {
    val posterAspectRatio = 2f / 3f
    SubcomposeAsyncImage(
        model = imageUrl, contentDescription = null, modifier = modifier
    ) {
        val state = painter.state
        if (state is AsyncImagePainter.State.Loading || state is AsyncImagePainter.State.Error) {
            // Show a grey placeholder while loading or if there's an error
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.35f) // Take 50% of the screen width
                    .aspectRatio(posterAspectRatio) // Maintain 2:3 aspect ratio
                    .background(Color.LightGray)
            )
        } else {
            // Display the image once it's loaded
            SubcomposeAsyncImageContent()
        }
    }
}

@Composable
fun YearHeader(year: Int) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Year: $year",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
    }
}