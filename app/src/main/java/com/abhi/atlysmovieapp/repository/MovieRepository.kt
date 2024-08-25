package com.abhi.atlysmovieapp.repository

import android.util.Log
import com.abhi.atlysmovieapp.model.Movie
import com.abhi.atlysmovieapp.util.dummyMovies
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieRepository {

    fun getMovies(): Flow<Result<List<Movie>>> = flow {

        try {
            val response = movieApiService.getMovies()
            if (response.isSuccessful) {
                val movies = response.body() ?: emptyList()
                emit(Result.success(movies))
            } else {
                emit(Result.failure(Exception("Error ${response.code()}: ${response.message()}")))
            }
        } catch (e: Exception) {
            Log.d("TAG", "getMovies: ${e}")
            emit(Result.failure(e))
        }
    }
}