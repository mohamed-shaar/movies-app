package com.company.moviesapp.detailsscreen.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.moviesapp.detailsscreen.domain.usecase.GetMovieDetailsScreenUseCase
import com.company.moviesapp.detailsscreen.presentation.model.MovieDetailsDisplayModel
import com.company.moviesapp.shared.data.local.datasource.WatchLaterLocalDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsScreen: GetMovieDetailsScreenUseCase,
    private val watchLaterLocalDataSource: WatchLaterLocalDataSource
) : ViewModel() {

    private val _moviesState = MutableStateFlow<MovieDetailsUiState>(MovieDetailsUiState.Loading)
    val moviesState: StateFlow<MovieDetailsUiState> get() = _moviesState.asStateFlow()

    fun getData(id: String) {
        viewModelScope.launch {
            getMovieDetails(id)
        }
    }

    private suspend fun getMovieDetails(id: String) {
        try {
            _moviesState.value = MovieDetailsUiState.Loading
            val movieDetails: MovieDetailsDisplayModel =
                getMovieDetailsScreen.getMovieDetailsScreen(id)
            _moviesState.value = MovieDetailsUiState.Success(movieDetails)
        } catch (e: Exception) {
            println(e.toString())
            _moviesState.value = MovieDetailsUiState.Error
        }
    }

    // Toggle the "Watch Later" status
    fun toggleWatchLater() {
        val currentState = _moviesState.value
        if (currentState is MovieDetailsUiState.Success) {
            val movieDetails = currentState.movieDetailsDisplayModel
            val isAdded = !movieDetails.addToWatch // Toggle the status

            viewModelScope.launch {
                if (isAdded) {
                    watchLaterLocalDataSource.addToWatchLater(movieDetails.id)
                } else {
                    watchLaterLocalDataSource.removeFromWatchLater(movieDetails.id)
                }

                // Update the UI state
                val updatedMovieDetails = movieDetails.copy(addToWatch = isAdded)
                _moviesState.value = MovieDetailsUiState.Success(updatedMovieDetails)
            }
        }
    }
}

sealed interface MovieDetailsUiState {
    data class Success(
        val movieDetailsDisplayModel: MovieDetailsDisplayModel
    ) : MovieDetailsUiState

    data object Error : MovieDetailsUiState
    data object Loading : MovieDetailsUiState
}
