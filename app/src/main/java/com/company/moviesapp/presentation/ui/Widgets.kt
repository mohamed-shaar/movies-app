package com.company.moviesapp.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.company.moviesapp.presentation.models.MovieDisplayModel
import com.company.moviesapp.presentation.viewmodel.MovieUiState
import com.company.moviesapp.presentation.viewmodel.MoviesViewModel
import kotlinx.coroutines.runBlocking

@Composable
fun MovieList(moviesViewModel: MoviesViewModel) {

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
            val movieList = (moviesState as MovieUiState.Success).movies
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(movieList) { movie ->
                    MovieItem(movie)
                }
            }
        }

        else -> {
            return Button(onClick = { runBlocking { moviesViewModel.getPopularMovies() } }) {
                Text("Retry")
            }
        }
    }


}

@Composable
fun MovieItem(movie: MovieDisplayModel) {
    Box(
        modifier = Modifier
            .padding(16.dp)
            .border(
                border = BorderStroke(1.dp, Color.Black), shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Row {
            ImageWithPlaceholder(
                imageUrl = movie.image,
                modifier = Modifier
                    .padding(8.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )
            Column {
                Text(
                    text = movie.title, modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(text = movie.overview)
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