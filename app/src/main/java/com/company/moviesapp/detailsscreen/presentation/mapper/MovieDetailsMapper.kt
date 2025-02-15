package com.company.moviesapp.detailsscreen.presentation.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.company.moviesapp.detailsscreen.data.remote.dto.Cast
import com.company.moviesapp.detailsscreen.data.remote.dto.Crew
import com.company.moviesapp.detailsscreen.data.remote.dto.MovieCreditsResponse
import com.company.moviesapp.detailsscreen.data.remote.dto.MovieDetailsResponse
import com.company.moviesapp.detailsscreen.presentation.model.CastDisplayModel
import com.company.moviesapp.detailsscreen.presentation.model.MovieDetailsDisplayModel
import com.company.moviesapp.listscreen.presentation.model.MovieDisplayModel
import com.company.moviesapp.shared.data.local.datasource.WatchLaterLocalDataSource

interface MovieDetailsMapper {
    fun mapMovieDetailsResponse(
        movieDetailsResponse: MovieDetailsResponse,
        movieCreditsResponse: MovieCreditsResponse,
        similarMovies: List<MovieDisplayModel>,
        similarMoviesCreditsResponse: List<MovieCreditsResponse>,
        watchLaterLocalDataSource: WatchLaterLocalDataSource
    ): MovieDetailsDisplayModel
}

class MovieDetailsMapperImpl : MovieDetailsMapper {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun mapMovieDetailsResponse(
        movieDetailsResponse: MovieDetailsResponse,
        movieCreditsResponse: MovieCreditsResponse,
        similarMovies: List<MovieDisplayModel>,
        similarMoviesCreditsResponse: List<MovieCreditsResponse>,
        watchLaterLocalDataSource: WatchLaterLocalDataSource
    ): MovieDetailsDisplayModel {
        val filteredActors: MutableList<Cast> = mutableListOf()
        val filteredDirectors: MutableList<Crew> = mutableListOf()

        for (movieCredits in similarMoviesCreditsResponse) {
            // Get a list of Cast objects where knownForDepartment is "Acting"
            filteredActors.addAll(movieCredits.cast.filter {
                it.knownForDepartment.equals(
                    "Acting",
                    ignoreCase = true
                )
            })

            // Get a list of Crew objects where knownForDepartment is "Directing"
            filteredDirectors.addAll(movieCredits.crew.filter {
                it.knownForDepartment.equals(
                    "Directing",
                    ignoreCase = true
                )
            })
        }

        val uniqueActors = filteredActors.distinctBy { it.id }
        val uniqueDirectors = filteredDirectors.distinctBy { it.id }

        var sortedActors: List<Cast> = uniqueActors.sortedByDescending { it.popularity }
        var sortedDirectors: List<Crew> = uniqueDirectors.sortedByDescending { it.popularity }

        sortedActors = sortedActors.take(5)
        sortedDirectors = sortedDirectors.take(5)

        val actors: MutableList<CastDisplayModel> = mutableListOf()
        val directors: MutableList<CastDisplayModel> = mutableListOf()

        for (actor in sortedActors) {
            actors.add(
                CastDisplayModel(
                    name = actor.name,
                    job = actor.knownForDepartment,
                    popularity = actor.popularity,
                    profilePath = if (actor.profilePath != null) "https://image.tmdb.org/t/p/w300${actor.profilePath}" else "",
                )
            )
        }

        for (director in sortedDirectors) {
            directors.add(
                CastDisplayModel(
                    name = director.name,
                    job = director.knownForDepartment,
                    popularity = director.popularity,
                    profilePath = if (director.profilePath != null) "https://image.tmdb.org/t/p/w300${director.profilePath}" else "",
                )
            )
        }

        return MovieDetailsDisplayModel(
            id = movieDetailsResponse.id.toString(),
            title = movieDetailsResponse.title,
            overview = movieDetailsResponse.overview,
            image = "https://image.tmdb.org/t/p/w300${movieDetailsResponse.posterPath}",
            addToWatch = watchLaterLocalDataSource.isInWatchLater(movieDetailsResponse.id.toString()),
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