package com.app.data.module

import com.app.data.network.MovieReviewApiService
import com.app.data.repository.MovieReviewRepository
import com.app.data.storage.MovieReviewDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object MovieRepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideMovieRepository(
        movieReviewApiService: MovieReviewApiService,
        movieReviewDao: MovieReviewDao,
        coroutineDispatcher: CoroutineDispatcher
    ): MovieReviewRepository {
        return MovieReviewRepository(movieReviewApiService, movieReviewDao, coroutineDispatcher)
    }
}
