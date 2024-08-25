package com.abhi.atlysmovieapp.repository

import com.abhi.atlysmovieapp.model.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieApiService {

    @GET("Random/20")
    suspend fun getMovies(): Response<List<Movie>>
}