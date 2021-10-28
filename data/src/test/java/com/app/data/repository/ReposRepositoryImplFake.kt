package com.app.data.repository

import androidx.paging.PagingData
import com.app.data.storage.MovieReviewEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ReposRepositoryImplFake : IMovieReviewRepository {
    val listReposEntity = ReposEntityProvider.createListRepoEntity()

    override fun getAllMovies(): Flow<PagingData<MovieReviewEntity>> {
        return flow {
            emit(
                PagingData.from(
                    listReposEntity
                )
            )
        }
    }
}
