package com.company.moviesapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.company.moviesapp.presentation.models.MovieDetailsDisplayModel
import com.company.moviesapp.presentation.usecase.GetMovieDetailsScreenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsScreen: GetMovieDetailsScreenUseCase
) : ViewModel(), ViewModelProvider.Factory {

    private val _moviesState = MutableStateFlow<MovieDetailsUiState>(MovieDetailsUiState.Loading)
    val moviesState: StateFlow<MovieDetailsUiState> get() = _moviesState.asStateFlow()

    fun getDate(id: String) {
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
}

sealed interface MovieDetailsUiState {
    data class Success(
        val movieDetailsDisplayModel: MovieDetailsDisplayModel
    ) : MovieDetailsUiState

    data object Error : MovieDetailsUiState
    data object Loading : MovieDetailsUiState
}
