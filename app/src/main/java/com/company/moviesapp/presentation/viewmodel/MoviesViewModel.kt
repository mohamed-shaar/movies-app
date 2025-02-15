package com.company.moviesapp.presentation.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.company.moviesapp.data.local.datasource.WatchLaterLocalDataSource
import com.company.moviesapp.data.remote.datasource.popularmovies.PopularMoviesRemoteDataSource
import com.company.moviesapp.data.remote.datasource.searchmovies.SearchMoviesRemoteDataSource
import com.company.moviesapp.presentation.mappers.MovieMapperImpl
import com.company.moviesapp.presentation.models.GroupedMovieList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(FlowPreview::class)
@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val popularMoviesDataSource: PopularMoviesRemoteDataSource,
    private val searchMoviesDataSource: SearchMoviesRemoteDataSource,
    private val watchLaterDataSource: WatchLaterLocalDataSource,
    private val movieMapper: MovieMapperImpl
) : ViewModel(), ViewModelProvider.Factory {

    private var pageNumber: Int = 1

    private val _moviesState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val moviesState: StateFlow<MovieUiState> get() = _moviesState.asStateFlow()

    private val _textSearch = MutableStateFlow("")
    private val textSearch: StateFlow<String> = _textSearch.asStateFlow()

    init {
        observeSearchText()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeSearchText() {
        viewModelScope.launch {
            textSearch
                .debounce(2000)
                .distinctUntilChanged()
                .drop(1)
                .collect { query ->
                    callSearchMovie(query)
                }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getData() {
        viewModelScope.launch {
            getPopularMovies()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun getPopularMovies() {
        try {
            _moviesState.value = MovieUiState.Loading
            val moviesResponse = popularMoviesDataSource.getMovies(pageNumber)
            val movies = moviesResponse.results.map { movie ->
                movieMapper.mapPopularMovieToMovieDisplayModel(
                    movie,
                    watchLaterDataSource.isInWatchLater(movie.id.toString())
                )
            }
            val groupedMovies = movieMapper.groupMoviesByYear(movies)
            _moviesState.value = MovieUiState.Success(groupedMovies)
        } catch (e: Exception) {
            _moviesState.value = MovieUiState.Error
        }
    }

    fun setSearchText(query: String) {
        _textSearch.value = query
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private suspend fun callSearchMovie(query: String) {
        try {
            if (query.isEmpty()) {
                getPopularMovies()
                return
            }
            _moviesState.value = MovieUiState.Loading
            val searchMoviesResponse = searchMoviesDataSource.searchMovies(query, pageNumber)
            val movies = searchMoviesResponse.results.map { movie ->
                movieMapper.mapSearchMovieToMovieDisplayModel(
                    movie,
                    watchLaterDataSource.isInWatchLater(movie.id.toString())
                )
            }
            val groupedMovies = movieMapper.groupMoviesByYear(movies)
            _moviesState.value = MovieUiState.Success(groupedMovies)
        } catch (e: Exception) {
            _moviesState.value = MovieUiState.Error
        }
    }

    fun toggleWatchLater(movieId: String, isAdded: Boolean) {
        viewModelScope.launch {
            if (isAdded) {
                watchLaterDataSource.addToWatchLater(movieId)
            } else {
                watchLaterDataSource.removeFromWatchLater(movieId)
            }
            updateMovieWatchLaterStatus(movieId, isAdded)
        }
    }

    private fun updateMovieWatchLaterStatus(id: String, isAdded: Boolean) {
        val currentState = _moviesState.value
        if (currentState is MovieUiState.Success) {
            val updatedGroups = currentState.movies.map { group ->
                val updatedMovies = group.movies.map { movie ->
                    if (movie.id == id) movie.copy(addToWatch = isAdded) else movie
                }
                group.copy(movies = updatedMovies)
            }
            _moviesState.value = MovieUiState.Success(updatedGroups)
        }
    }

    fun refreshMovies() {
        val currentState = _moviesState.value
        if (currentState is MovieUiState.Success) {
            val updatedGroups = currentState.movies.map { group ->
                val updatedMovies = group.movies.map { movie ->
                    movie.copy(addToWatch = watchLaterDataSource.isInWatchLater(movie.id))
                }
                group.copy(movies = updatedMovies)
            }
            _moviesState.value = MovieUiState.Success(updatedGroups)
        }
    }
}
sealed interface MovieUiState {
    data class Success(
        val movies: List<GroupedMovieList>
    ) : MovieUiState

    data object Error : MovieUiState
    data object Loading : MovieUiState
}
