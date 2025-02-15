package com.company.moviesapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.company.moviesapp.presentation.models.MovieDetailsDisplayModel
import com.company.moviesapp.presentation.usecase.GetMovieDetailsScreenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieDetailsViewModel(
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

class MovieDetailsViewModelFactory(
    private val getMovieDetailsScreen: GetMovieDetailsScreenUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieDetailsViewModel(
                getMovieDetailsScreen
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

sealed interface MovieDetailsUiState {
    data class Success(
        val movieDetailsDisplayModel: MovieDetailsDisplayModel
    ) : MovieDetailsUiState

    data object Error : MovieDetailsUiState
    data object Loading : MovieDetailsUiState
}
