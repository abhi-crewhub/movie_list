package com.abhi.atlysmovieapp.model

data class MovieDetailsResponse(
    val Plot: String,
    val Poster: String,
    val Title: String,
    val imdbID: String,
    val imdbRating: String
)