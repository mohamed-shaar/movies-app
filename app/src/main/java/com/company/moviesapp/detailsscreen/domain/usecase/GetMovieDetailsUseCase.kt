package com.company.moviesapp.detailsscreen.domain.usecase

import com.company.moviesapp.detailsscreen.data.remote.datasource.moviecredits.MovieCreditsRemoteDataSource
import com.company.moviesapp.detailsscreen.data.remote.datasource.moviedetails.MovieDetailsRemoteDataSource
import com.company.moviesapp.detailsscreen.data.remote.datasource.similarmovies.SimilarMoviesRemoteDataSource
import com.company.moviesapp.detailsscreen.data.remote.dto.MovieCreditsResponse
import com.company.moviesapp.detailsscreen.presentation.mapper.MovieDetailsMapper
import com.company.moviesapp.detailsscreen.presentation.model.MovieDetailsDisplayModel
import com.company.moviesapp.listscreen.presentation.model.MovieDisplayModel
import com.company.moviesapp.shared.data.local.datasource.WatchLaterLocalDataSource
import com.company.moviesapp.shared.utils.parseDate

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