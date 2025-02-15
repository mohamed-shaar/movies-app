package com.company.moviesapp.presentation.mappers

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.company.moviesapp.data.remote.dto.PopularMovie
import com.company.moviesapp.data.remote.dto.SearchMovie
import com.company.moviesapp.presentation.models.GroupedMovieList
import com.company.moviesapp.presentation.models.MovieDisplayModel
import com.company.moviesapp.presentation.parseDate
import java.time.ZoneId
import javax.inject.Inject

interface MovieMapper {
    fun mapPopularMovieToMovieDisplayModel(
        movie: PopularMovie,
        isInWatchLater: Boolean
    ): MovieDisplayModel

    fun mapSearchMovieToMovieDisplayModel(
        movie: SearchMovie,
        isInWatchLater: Boolean
    ): MovieDisplayModel
}

class MovieMapperImpl @Inject constructor() : MovieMapper {
    @SuppressLint("NewApi")
    override fun mapPopularMovieToMovieDisplayModel(
        movie: PopularMovie,
        isInWatchLater: Boolean
    ): MovieDisplayModel {
        return MovieDisplayModel(
            id = movie.id.toString(),
            title = movie.title,
            overview = movie.overview,
            image = "https://image.tmdb.org/t/p/w300${movie.posterPath}",
            addToWatch = isInWatchLater,
            releaseDate = parseDate(dateString = movie.releaseDate)
        )
    }

    @SuppressLint("NewApi")
    override fun mapSearchMovieToMovieDisplayModel(
        movie: SearchMovie,
        isInWatchLater: Boolean
    ): MovieDisplayModel {
        return MovieDisplayModel(
            id = movie.id.toString(),
            title = movie.title,
            overview = movie.overview,
            image = "https://image.tmdb.org/t/p/w300${movie.posterPath}",
            addToWatch = isInWatchLater,
            releaseDate = parseDate(dateString = movie.releaseDate)
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun groupMoviesByYear(movies: List<MovieDisplayModel>): List<GroupedMovieList> {
        val groupedMap = movies.groupBy { movie ->
            movie.releaseDate?.toInstant()?.atZone(ZoneId.systemDefault())?.year ?: 0
        }
        return groupedMap.map { (year, movieList) ->
            GroupedMovieList(year, movieList)
        }.sortedByDescending { it.year }
    }
}