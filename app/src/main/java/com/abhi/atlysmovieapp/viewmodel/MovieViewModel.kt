package com.abhi.atlysmovieapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abhi.atlysmovieapp.model.MovieDetailsResponse
import com.abhi.atlysmovieapp.model.Search
import com.abhi.atlysmovieapp.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _movies = MutableStateFlow<Result<List<Search>>>(Result.success(emptyList()))

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val movies: StateFlow<List<Search>> = _movies
        .combine(searchQuery) { moviesResult, query ->
            // Filter movies based on the search query
            if (query.isBlank()) {
                moviesResult.getOrNull() ?: emptyList()
            } else {
                moviesResult.getOrNull()?.filter {
                    it.Title.contains(query, ignoreCase = true)
                } ?: emptyList()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _movieDetail = MutableStateFlow<Result<MovieDetailsResponse?>>(Result.success(null))
    val movieDetail: StateFlow<Result<MovieDetailsResponse?>> = _movieDetail.asStateFlow()

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
            Log.d("MovieViewModel", "Fetching details for movieId: $movieId")
            _loading.value = true
            _error.value = null
            movieRepository.getMovieDetail(movieId)
                .onStart { Log.d("MovieViewModel", "Fetching movie detail from repository") }
                .onEach { result ->
                    _loading.value = false
                    Log.d("MovieViewModel", "Movie detail fetched successfully: $result")
                }
                .catch { e ->
                    _loading.value = false
                    _error.value = e.message
                    Log.e("MovieViewModel", "Error fetching movie detail", e)
                }
                .collect { result ->
                    _movieDetail.value = result
                    Log.d("MovieViewModel", "Movie detail result updated: $result")
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}