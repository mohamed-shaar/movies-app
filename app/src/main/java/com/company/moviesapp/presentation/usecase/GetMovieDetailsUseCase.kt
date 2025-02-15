package com.company.moviesapp.presentation.usecase

import com.company.moviesapp.data.local.datasource.WatchLaterLocalDataSource
import com.company.moviesapp.data.remote.datasource.moviecredits.MovieCreditsRemoteDataSource
import com.company.moviesapp.data.remote.datasource.moviedetails.MovieDetailsRemoteDataSource
import com.company.moviesapp.data.remote.datasource.similarmovies.SimilarMoviesRemoteDataSource
import com.company.moviesapp.data.remote.dto.MovieCreditsResponse
import com.company.moviesapp.presentation.mappers.MovieDetailsMapper
import com.company.moviesapp.presentation.models.MovieDetailsDisplayModel
import com.company.moviesapp.presentation.models.MovieDisplayModel
import com.company.moviesapp.presentation.parseDate

interface GetMovieDetailsScreenUseCase {
    suspend fun getMovieDetailsScreen(id: String): MovieDetailsDisplayModel
}

class GetMovieDetailsScreenUseCaseImpl(
    private val movieDetailsRemoteDataSource: MovieDetailsRemoteDataSource,
    private val movieCreditsRemoteDataSource: MovieCreditsRemoteDataSource,
    private val similarMoviesRemoteDataSource: SimilarMoviesRemoteDataSource,
    private val movieDetailsMapper: MovieDetailsMapper,
    private val watchLaterLocalDataSource: WatchLaterLocalDataSource
) : GetMovieDetailsScreenUseCase {
    override suspend fun getMovieDetailsScreen(id: String): MovieDetailsDisplayModel {
        val movieDetailsResponse = movieDetailsRemoteDataSource.getMovieDetails(id)
        val movieCreditsResponse = movieCreditsRemoteDataSource.getMovieCredits(id)
        val similarMoviesResponse =
            similarMoviesRemoteDataSource.getSimilarMovies(id)

        val similarMovies: List<MovieDisplayModel> =
            similarMoviesResponse.results.take(5).map { similarMovie ->
                MovieDisplayModel(
                    id = similarMovie.id.toString(),
                    title = similarMovie.title,
                    overview = similarMovie.overview,
                    image = "https://image.tmdb.org/t/p/w300${similarMovie.posterPath}",
                    addToWatch = false,
                    releaseDate = parseDate(dateString = similarMovie.releaseDate)
                )
            }.toList()


        val similarMoviesCreditsResponse: MutableList<MovieCreditsResponse> = mutableListOf()
        for (movie in similarMovies) {
            val movieCredits = movieCreditsRemoteDataSource.getMovieCredits(movie.id)
            similarMoviesCreditsResponse.add(movieCredits)
        }

        val movieDetails: MovieDetailsDisplayModel = movieDetailsMapper.mapMovieDetailsResponse(
            movieDetailsResponse,
            movieCreditsResponse,
            similarMovies,
            similarMoviesCreditsResponse,
            watchLaterLocalDataSource
        )
        return movieDetails
    }
}