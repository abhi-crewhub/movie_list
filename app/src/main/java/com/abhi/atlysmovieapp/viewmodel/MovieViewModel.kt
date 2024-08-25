package com.abhi.atlysmovieapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhi.atlysmovieapp.model.Movie
import com.abhi.atlysmovieapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _movies = MutableStateFlow<Result<List<Movie>>>(Result.success(emptyList()))

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _movieDetail = MutableStateFlow<Result<Movie?>>(Result.success(null))
    val movieDetail: StateFlow<Result<Movie?>> = _movieDetail.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()



    fun fetchMovies() {
        viewModelScope.launch {
            Log.d("MovieViewModel", "Fetching movies started")
            _loading.value = true
            _error.value = null
            movieRepository.getMovies()
                .onStart { Log.d("MovieViewModel", "Fetching movies from repository") }
                .onEach { result ->
                    _loading.value = false
                    if (result.isFailure) {
                        Log.e("MovieViewModel", "Error fetching movies: ${result.exceptionOrNull()}")
                        _error.value = result.exceptionOrNull().toString()
                    } else {
                        Log.d("MovieViewModel", "Movies fetched successfully: ${result.getOrNull()}")
                        _movies.value = result
                    }
                }
                .catch { e ->
                    _loading.value = false
                    _error.value = e.message
                }
                .collect { result ->

                }
        }
    }

    fun fetchMovieDetail(movieId: String) {
        viewModelScope.launch {
            val moviesList = _movies.value.getOrNull() ?: emptyList()
            val movie = moviesList.find { it.id == movieId.toInt() }
            _movieDetail.value = if (movie != null) {
                Result.success(movie)
            } else {
                Result.failure(Exception("Movie not found"))
            }
        }
    }
}

