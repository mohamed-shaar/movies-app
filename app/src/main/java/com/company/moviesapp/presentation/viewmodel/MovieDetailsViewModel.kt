package com.company.moviesapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.company.moviesapp.data.remote.datasource.moviecredits.MovieCreditsRemoteDataSource
import com.company.moviesapp.data.remote.datasource.moviedetails.MovieDetailsRemoteDataSource
import com.company.moviesapp.data.remote.datasource.similarmovies.SimilarMoviesRemoteDataSource
import com.company.moviesapp.presentation.mappers.MovieDetailsMapper
import com.company.moviesapp.presentation.models.MovieDetailsDisplayModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MovieDetailsViewModel(
    private val movieDetailsRemoteDataSource: MovieDetailsRemoteDataSource,
    private val movieCreditsRemoteDataSource: MovieCreditsRemoteDataSource,
    private val similarMoviesRemoteDataSource: SimilarMoviesRemoteDataSource,
    private val movieDetailsMapper: MovieDetailsMapper,
) : ViewModel(), ViewModelProvider.Factory {

    private val _moviesState = MutableStateFlow<MovieDetailsUiState>(MovieDetailsUiState.Loading)
    val moviesState: StateFlow<MovieDetailsUiState> get() = _moviesState.asStateFlow()

    suspend fun getMovieDetailsScreen(id: Int) {
        try {
            _moviesState.value = MovieDetailsUiState.Loading
            val movieDetailsResponse = movieDetailsRemoteDataSource.getMovieDetails(id.toString())
            val movieCreditsResponse = movieCreditsRemoteDataSource.getMovieCredits(id.toString())
            val similarMoviesResponse =
                similarMoviesRemoteDataSource.getSimilarMovies(id.toString())

            val movieDetails: MovieDetailsDisplayModel = movieDetailsMapper.mapMovieDetailsResponse(
                movieDetailsResponse,
                movieCreditsResponse,
                similarMoviesResponse
            )

            _moviesState.value = MovieDetailsUiState.Success(movieDetails)
        } catch (e: Exception) {
            println(e.toString())
            _moviesState.value = MovieDetailsUiState.Error
        }
    }
}

class MovieDetailsViewModelFactory(
    private val movieDetailsRemoteDataSource: MovieDetailsRemoteDataSource,
    private val movieCreditsRemoteDataSource: MovieCreditsRemoteDataSource,
    private val similarMoviesRemoteDataSource: SimilarMoviesRemoteDataSource,
    private val movieDetailsMapper: MovieDetailsMapper,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(MovieDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieDetailsViewModel(
                movieDetailsRemoteDataSource,
                movieCreditsRemoteDataSource,
                similarMoviesRemoteDataSource,
                movieDetailsMapper,
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
