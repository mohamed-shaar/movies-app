package com.company.moviesapp.listscreen.presentation.model

import java.util.Date

data class MovieDisplayModel(
    val id: String,
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
