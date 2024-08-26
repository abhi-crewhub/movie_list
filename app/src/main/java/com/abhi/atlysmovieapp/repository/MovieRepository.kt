package com.abhi.atlysmovieapp.repository

import android.net.http.HttpException
import android.util.Log
import com.abhi.atlysmovieapp.model.MovieDetailsResponse
import com.abhi.atlysmovieapp.model.Search
//import com.abhi.atlysmovieapp.util.dummyMovies
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieApiService: MovieApiService
) {

    fun getMovies(): Flow<Result<List<Search>>> = flow {
//        kotlinx.coroutines.delay(1000)
//        emit(Result.success(dummyMovies))

        //TODO needed to be uncomment when proper URL given meanwhile dummy data
        try {
            val response = movieApiService.getMovies()
            if (response.isSuccessful) {
                Log.d("TAG", "getMovies: ${response.body()}")
                val movies = response.body()?.Search ?: emptyList<Search>()
                emit(Result.success(movies))
            } else {
                emit(Result.failure(Exception("Error ${response.code()}: ${response.message()}")))
            }
        } catch (e: Exception) {
            Log.d("TAG", "getMovies: ${e}")
            emit(Result.failure(e))
        }
    }


    fun getMovieDetail(id: String): Flow<Result<MovieDetailsResponse?>> = flow {
        try {
            val response = movieApiService.getMovieDetails(id)
            if (response.isSuccessful) {
                emit(Result.success(response.body()))
            } else {
                emit(Result.failure(Exception("Error: ${response.code()}")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}