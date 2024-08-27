package com.abhi.atlysmovieapp.model

data class MovieResponse(
    val Response: String,
    val Search: List<Movie>,
    val totalResults: String
)