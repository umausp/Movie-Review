package com.app.moviereview.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.app.data.repository.MovieReviewRepository
import com.app.data.storage.MovieReviewEntity
import com.app.data.utils.launchPagingAsync
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
class MovieReviewMainViewModel @ExperimentalPagingApi
@Inject constructor(private val movieReviewRepository: MovieReviewRepository) : ViewModel() {

    private lateinit var _movieReviewFlow: Flow<PagingData<MovieReviewEntity>>

    val showLoadingToUI = MutableLiveData<Boolean>()
    val showErrorUI = MutableLiveData<Boolean>()
    val selectedReview = MutableLiveData<MovieReviewEntity>()

    val movieReviewFlow: Flow<PagingData<MovieReviewEntity>>
        get() = _movieReviewFlow

    fun getAllMovieReview() = launchPagingAsync(viewModelScope, {
        movieReviewRepository.getAllMovies().cachedIn(viewModelScope)
    }, { success ->
        _movieReviewFlow = success
    }) {
        showErrorUI.value = true
    }
}
