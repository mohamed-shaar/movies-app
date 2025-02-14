package com.company.moviesapp.presentation.models

data class MovieDetailsDisplayModel(
    val title: String,
    val overview: String,
    val image: String,
    val addToWatch: Boolean,
    val tagline: String,
    val revenue: Long,
    val releaseDate: String,
    val status: String,
    val cast: List<CastDisplayModel>,
    val crew: List<CastDisplayModel>,
    val similarMovies: List<MovieDisplayModel>
)

data class CastDisplayModel(
    val name: String,
    val job: String,
    val popularity: Double,
    val profilePath: String
)
