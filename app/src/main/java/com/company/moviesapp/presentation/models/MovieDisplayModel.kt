package com.company.moviesapp.presentation.models

import java.time.LocalDate

data class MovieDisplayModel(
    val title: String,
    val overview: String,
    val image: String,
    val addToWatch: Boolean,
    val releaseDate: LocalDate?
)
