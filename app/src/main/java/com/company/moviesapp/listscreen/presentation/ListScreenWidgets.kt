package com.company.moviesapp.listscreen.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.company.moviesapp.listscreen.presentation.viewmodel.MovieUiState
import com.company.moviesapp.listscreen.presentation.viewmodel.MoviesViewModel
import com.company.moviesapp.shared.presentation.ui.MovieItem

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieList(
    moviesViewModel: MoviesViewModel,
    onClick: (String) -> Unit,
    onSearch: (String) -> Unit,
    onToggleWatchLater: (String, Boolean) -> Unit
) {

    val moviesState by moviesViewModel.moviesState.collectAsStateWithLifecycle()
    Column {
        SimpleOutlinedTextFieldSample(
            onCall = { input ->
                onSearch(
                    input,
                )
            }
        )
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
                            MovieItem(movie, onClick, onToggleWatchLater = { isAdded ->
                                onToggleWatchLater(movie.id, isAdded)
                            })
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
fun YearHeader(year: Int) {
    Surface(
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
    ) {
        Text(
            text = "Year: $year",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
    }
}