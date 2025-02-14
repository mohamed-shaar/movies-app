package com.company.moviesapp.presentation.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.company.moviesapp.data.remote.dto.MovieCreditsResponse
import com.company.moviesapp.data.remote.dto.MovieDetailsResponse
import com.company.moviesapp.data.remote.dto.SimilarMoviesResponse
import com.company.moviesapp.presentation.models.CastDisplayModel
import com.company.moviesapp.presentation.models.MovieDetailsDisplayModel
import com.company.moviesapp.presentation.models.MovieDisplayModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

interface MovieDetailsMapper {
    fun mapMovieDetailsResponse(
        movieDetailsResponse: MovieDetailsResponse,
        movieCreditsResponse: MovieCreditsResponse,
        similarMoviesResponse: SimilarMoviesResponse
    ): MovieDetailsDisplayModel
}

class MovieDetailsMapperImpl : MovieDetailsMapper {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun mapMovieDetailsResponse(
        movieDetailsResponse: MovieDetailsResponse,
        movieCreditsResponse: MovieCreditsResponse,
        similarMoviesResponse: SimilarMoviesResponse
    ): MovieDetailsDisplayModel {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val actors: List<CastDisplayModel> = emptyList()
        val directors: List<CastDisplayModel> = emptyList()

        val similarMovies: List<MovieDisplayModel> =
            similarMoviesResponse.results.take(5).map { similarMovie ->
                MovieDisplayModel(
                    title = similarMovie.title,
                    overview = similarMovie.overview,
                    image = "https://image.tmdb.org/t/p/w300${similarMovie.posterPath}",
                    addToWatch = false,
                    releaseDate = LocalDate.parse(similarMovie.releaseDate, formatter)
                )
            }.toList()


        return MovieDetailsDisplayModel(
            title = movieDetailsResponse.title,
            overview = movieDetailsResponse.overview,
            image = movieDetailsResponse.posterPath,
            addToWatch = false,
            tagline = movieDetailsResponse.tagline,
            revenue = movieDetailsResponse.revenue,
            releaseDate = movieDetailsResponse.releaseDate,
            status = movieDetailsResponse.status,
            cast = actors,
            crew = directors,
            similarMovies = similarMovies
        )
    }

}