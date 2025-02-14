package com.company.moviesapp.presentation.models

import java.util.Date

data class MovieDisplayModel(
    val title: String,
    val overview: String,
    val image: String,
    val addToWatch: Boolean,
    val releaseDate: Date?
)

data class GroupedMovieList(
    val year: Int, // The year of the group
    val movies: List<MovieDisplayModel> // List of movies in this group
)
