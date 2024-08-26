package com.abhi.atlysmovieapp.repository

import com.abhi.atlysmovieapp.model.MovieDetailsResponse
import com.abhi.atlysmovieapp.model.MovieResponse
import com.abhi.atlysmovieapp.util.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

//    val apiKey
//        get() = BuildConfig.API_KEY

    @GET("/")
    suspend fun getMovies(
        @Query("s") query: String = "india",
        @Query("apikey") apiKey: String = API_KEY
    ): Response<MovieResponse>

    @GET("/")
    suspend fun getMovieDetails(
        @Query("i") movieId: String,
        @Query("apikey") apiKey: String = API_KEY,
        @Query("plot") plot: String = "full"
    ): Response<MovieDetailsResponse>
}