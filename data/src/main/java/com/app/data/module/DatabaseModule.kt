package com.app.data.module

import android.content.Context
import androidx.room.Room
import com.app.data.storage.MovieReviewDao
import com.app.data.storage.MovieReviewDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideMovieReviewDao(
        database: MovieReviewDataBase
    ): MovieReviewDao {
        return database.getMovieReviewDao()
    }

    @Singleton
    @Provides
    fun provideDatabaseObj(
        @ApplicationContext context: Context
    ): MovieReviewDataBase {
        return Room.databaseBuilder(
            context,
            MovieReviewDataBase::class.java, "movie_review.db"
        ).build()
    }
}
