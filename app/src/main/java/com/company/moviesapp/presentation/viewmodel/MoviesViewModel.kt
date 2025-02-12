package com.company.moviesapp.presentation.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.company.moviesapp.data.remote.datasource.popularmovies.PopularMoviesRemoteDataSource
import com.company.moviesapp.data.remote.datasource.searchmovies.SearchMoviesRemoteDataSource
import com.company.moviesapp.data.remote.dto.PopularMoviesResponse
import com.company.moviesapp.data.remote.dto.SearchMoviesResponse
import com.company.moviesapp.presentation.models.MovieDisplayModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class MoviesViewModel(
    private val popularMoviesRemoteDataSource: PopularMoviesRemoteDataSource,
    private val searchMoviesRemoteDataSource: SearchMoviesRemoteDataSource
) : ViewModel(), ViewModelProvider.Factory {
    private var pageNumber: Int = 1

    private val _moviesState = MutableStateFlow<MovieUiState>(MovieUiState.Loading)
    val moviesState: StateFlow<MovieUiState> get() = _moviesState.asStateFlow()

    private val _textSearch = MutableStateFlow("")
    private val textSearch: StateFlow<String> = _textSearch.asStateFlow()

    init {
        viewModelScope.launch {
            // As soon the textSearch flow changes,
            // if the user stops typing for 1000ms, the item will be emitted
            textSearch.debounce(2000).collect { query ->
                callSearchMovie(query)
            }
        }
    }

    @SuppressLint("NewApi")
    suspend fun getPopularMovies() {
        val moviesResponse: PopularMoviesResponse =
            popularMoviesRemoteDataSource.getMovies(pageNumber = pageNumber)
        val movieList: MutableList<MovieDisplayModel> = mutableListOf()
        for (movie in moviesResponse.results) {
            movieList.add(
                MovieDisplayModel(
                    title = movie.title,
                    overview = movie.overview,
                    image = "https://image.tmdb.org/t/p/w300${movie.posterPath}",
                    addToWatch = false,
                    null
                )
            )
        }
        _moviesState.value = MovieUiState.Success(movieList)
    }

    fun setSearchText(it: String) {
        _textSearch.value = it
    }

    private suspend fun callSearchMovie(query: String) {
        if (query.isEmpty()) {
            getPopularMovies()
            return
        }
        _moviesState.value = MovieUiState.Loading
        val searchMoviesResponse: SearchMoviesResponse =
            searchMoviesRemoteDataSource.searchMovies(
                query = query,
                pageNumber = pageNumber
            )
        val movieList: MutableList<MovieDisplayModel> = mutableListOf()
        for (movie in searchMoviesResponse.results) {
            movieList.add(
                MovieDisplayModel(
                    title = movie.title,
                    overview = movie.overview,
                    image = "https://image.tmdb.org/t/p/w300${movie.posterPath}",
                    addToWatch = false,
                    null
                )
            )
        }
        _moviesState.value = MovieUiState.Success(movieList)
    }
}

class MoviesViewModelFactory(
    private val popularMoviesRemoteDataSource: PopularMoviesRemoteDataSource,
    private val searchMoviesRemoteDataSource: SearchMoviesRemoteDataSource
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MoviesViewModel(popularMoviesRemoteDataSource, searchMoviesRemoteDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

sealed interface MovieUiState {
    data class Success(
        val movies: List<MovieDisplayModel>
    ) : MovieUiState

    data object Error : MovieUiState
    data object Loading : MovieUiState
}
