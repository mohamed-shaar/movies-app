package com.company.moviesapp.presentation.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun parseDate(dateString: String): Date? {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale("en"))
    try {
        val date: Date? = formatter.parse(dateString)
        println("Parsed date: $date")
        return date
    } catch (e: Exception) {
        println("Error: The date string '$dateString' is not in the expected format 'yyyy-MM-dd'.")
        return null // Return null or handle the error as needed
    }
}