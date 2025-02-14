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
import com.company.moviesapp.presentation.models.GroupedMovieList
import com.company.moviesapp.presentation.models.MovieDisplayModel
import com.company.moviesapp.presentation.parseDate
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import java.time.ZoneId

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
            // if the user stops typing for 2000ms, the item will be emitted
            textSearch.debounce(2000).collect { query ->
                callSearchMovie(query)
            }
        }
    }

    fun getData() {
        viewModelScope.launch {
            getPopularMovies()
        }
    }

    @SuppressLint("NewApi")
    private suspend fun getPopularMovies() {
        try {
            _moviesState.value = MovieUiState.Loading
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
                        releaseDate = parseDate(dateString = movie.releaseDate)
                    )
                )
            }
            val groupedMap: Map<Int, List<MovieDisplayModel>> = movieList.groupBy { movie ->
                movie.releaseDate?.toInstant()?.atZone(ZoneId.systemDefault())?.year ?: 0
            }
            val groups = groupedMap.map { (year, movieList) ->
                GroupedMovieList(year, movieList)
            }.sortedByDescending { it.year }
            _moviesState.value = MovieUiState.Success(groups)
        } catch (e: Exception) {
            println(e)
            _moviesState.value = MovieUiState.Error
        }
    }

    fun setSearchText(it: String) {
        _textSearch.value = it
    }

    @SuppressLint("NewApi")
    private suspend fun callSearchMovie(query: String) {
        try {
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
            val groupedMap: Map<Int, List<MovieDisplayModel>> = movieList.groupBy { movie ->
                movie.releaseDate?.toInstant()?.atZone(ZoneId.systemDefault())?.year ?: 0
            }
            val groupedMovieList = groupedMap.map { (year, movieList) ->
                GroupedMovieList(year, movieList)
            }.sortedByDescending { it.year }
            _moviesState.value = MovieUiState.Success(groupedMovieList)
        } catch (e: Exception) {
            println(e.toString())
            _moviesState.value = MovieUiState.Error
        }
    }
}

class PopularMoviesViewModelFactory(
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
        val movies: List<GroupedMovieList>
    ) : MovieUiState

    data object Error : MovieUiState
    data object Loading : MovieUiState
}
