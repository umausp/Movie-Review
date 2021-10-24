package com.app.moviereview.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.data.model.MovieReviewModel
import com.app.data.repository.MovieReviewRepository
import com.app.data.utils.Resource
import com.app.data.utils.toResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieReviewMainViewModel @Inject constructor(private val movieReviewRepository: MovieReviewRepository) : ViewModel() {

    val movieReviewResourceLiveData = MutableLiveData<Resource<MovieReviewModel>>()

    init {
        getMovieReviewData()
    }

    private fun getMovieReviewData() {
        viewModelScope.launch {
            movieReviewRepository.getMovieReviews(0).catch {
                movieReviewResourceLiveData.value = it.toResource {
                    callRetry(it)
                }
            }.collect {
                movieReviewResourceLiveData.value = it
            }
        }
    }

    /**
     * This method calls the API again in order to get fresh data
     */
    private fun callRetry(throwable: Throwable) {
        getMovieReviewData()
    }
}
