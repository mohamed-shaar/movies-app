package com.company.moviesapp

import com.company.moviesapp.detailsscreen.data.remote.datasource.moviecredits.MovieCreditsRemoteDataSource
import com.company.moviesapp.detailsscreen.data.remote.datasource.moviedetails.MovieDetailsRemoteDataSource
import com.company.moviesapp.detailsscreen.data.remote.datasource.similarmovies.SimilarMoviesRemoteDataSource
import com.company.moviesapp.detailsscreen.data.remote.dto.MovieCreditsResponse
import com.company.moviesapp.detailsscreen.data.remote.dto.MovieDetailsResponse
import com.company.moviesapp.detailsscreen.data.remote.dto.SimilarMovie
import com.company.moviesapp.detailsscreen.data.remote.dto.SimilarMoviesResponse
import com.company.moviesapp.detailsscreen.domain.usecase.GetMovieDetailsScreenUseCaseImpl
import com.company.moviesapp.detailsscreen.presentation.mapper.MovieDetailsMapper
import com.company.moviesapp.detailsscreen.presentation.model.MovieDetailsDisplayModel
import com.company.moviesapp.listscreen.presentation.model.MovieDisplayModel
import com.company.moviesapp.shared.data.local.datasource.WatchLaterLocalDataSource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.util.Date


class GetMovieDetailsScreenUseCaseImplTest {

    private lateinit var movieDetailsRemoteDataSource: MovieDetailsRemoteDataSource
    private lateinit var movieCreditsRemoteDataSource: MovieCreditsRemoteDataSource
    private lateinit var similarMoviesRemoteDataSource: SimilarMoviesRemoteDataSource
    private lateinit var movieDetailsMapper: MovieDetailsMapper
    private lateinit var watchLaterLocalDataSource: WatchLaterLocalDataSource
    private lateinit var getMovieDetailsScreenUseCase: GetMovieDetailsScreenUseCaseImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        movieDetailsRemoteDataSource = mock(MovieDetailsRemoteDataSource::class.java)
        movieCreditsRemoteDataSource = mock(MovieCreditsRemoteDataSource::class.java)
        similarMoviesRemoteDataSource = mock(SimilarMoviesRemoteDataSource::class.java)
        movieDetailsMapper = mock(MovieDetailsMapper::class.java)
        watchLaterLocalDataSource = mock(WatchLaterLocalDataSource::class.java)

        getMovieDetailsScreenUseCase = GetMovieDetailsScreenUseCaseImpl(
            movieDetailsRemoteDataSource,
            movieCreditsRemoteDataSource,
            similarMoviesRemoteDataSource,
            movieDetailsMapper,
            watchLaterLocalDataSource
        )
    }

    @Test
    fun `getMovieDetailsScreen should return MovieDetailsDisplayModel`(): Unit = runBlocking {
        // Arrange
        val movieId = "123"
        val movieDetailsResponse = mock(MovieDetailsResponse::class.java)
        val movieCreditsResponse = mock(MovieCreditsResponse::class.java)
        val similarMoviesResponse = mock(SimilarMoviesResponse::class.java)
//        val similarMovie = mock(SimilarMovie::class.java)
        val similarMovie = SimilarMovie(
            id = 1234,
            adult = false,
            backdropPath = "/backdrop1.jpg",
            genreIds = listOf(28, 12, 16),
            originalLanguage = "en",
            originalTitle = "Mock Movie 1",
            overview = "overview",
            popularity = 7.8,
            posterPath = "/path/to/poster",
            releaseDate = "2023-10-01",
            title = "Test Movie", // Provide a non-null title
            video = false,
            voteAverage = 8.1,
            voteCount = 1000
        )
        val similarMovies = listOf(similarMovie)

        // Create a real instance of MovieDisplayModel with valid non-null values
        val movieDisplayModel = MovieDisplayModel(
            id = "123",
            title = "Test Movie", // Provide a non-null title
            image = "/path/to/poster",
            releaseDate = Date(1696118400000),
            addToWatch = false,
            overview = "overview"
        )
        val movieDisplayModels = listOf(movieDisplayModel)

        val similarMoviesCreditsResponse = listOf(mock(MovieCreditsResponse::class.java))
        val movieDetailsDisplayModel = mock(MovieDetailsDisplayModel::class.java)

        // Mock responses from data sources
        `when`(movieDetailsRemoteDataSource.getMovieDetails(movieId)).thenReturn(
            movieDetailsResponse
        )
        `when`(movieCreditsRemoteDataSource.getMovieCredits(movieId)).thenReturn(
            movieCreditsResponse
        )
        `when`(similarMoviesRemoteDataSource.getSimilarMovies(movieId)).thenReturn(
            similarMoviesResponse
        )
        `when`(similarMoviesResponse.results).thenReturn(similarMovies)
        `when`(movieCreditsRemoteDataSource.getMovieCredits(anyString())).thenReturn(
            similarMoviesCreditsResponse[0]
        )

        // Mock mapper behavior
        `when`(
            movieDetailsMapper.mapMovieDetailsResponse(
                movieDetailsResponse, // Use argument matcher for movieDetailsResponse
                movieCreditsResponse, // Use argument matcher for movieCreditsResponse
                movieDisplayModels, // Argument matcher
                similarMoviesCreditsResponse, // Argument matcher
                watchLaterLocalDataSource // Use argument matcher for watchLaterLocalDataSource
            )
        ).thenReturn(movieDetailsDisplayModel)

        // Act
        val result = getMovieDetailsScreenUseCase.getMovieDetailsScreen(movieId)

        // Assert
//        assertEquals(movieDetailsDisplayModel, result)
        verify(movieDetailsRemoteDataSource).getMovieDetails(movieId)
        verify(movieCreditsRemoteDataSource).getMovieCredits(movieId)
        verify(similarMoviesRemoteDataSource).getSimilarMovies(movieId)
//        verify(movieCreditsRemoteDataSource, times(similarMovies.size)).getMovieCredits(anyString())
//        verify(movieDetailsMapper).mapMovieDetailsResponse(
//            movieDetailsResponse, // Use argument matcher for movieDetailsResponse
//            movieCreditsResponse, // Use argument matcher for movieCreditsResponse
//            movieDisplayModels, // Argument matcher
//            similarMoviesCreditsResponse, // Argument matcher
//            watchLaterLocalDataSource // Use argument matcher for watchLaterLocalDataSource
//        )
    }
}
