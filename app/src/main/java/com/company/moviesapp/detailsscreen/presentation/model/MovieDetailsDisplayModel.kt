package com.company.moviesapp.detailsscreen.presentation.model

import com.company.moviesapp.listscreen.presentation.model.MovieDisplayModel

data class MovieDetailsDisplayModel(
    val id: String,
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
