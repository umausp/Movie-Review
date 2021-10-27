package com.app.data.storage

import androidx.paging.PagingSource
import androidx.room.*
import com.app.data.model.Result
import com.google.gson.annotations.SerializedName

@Database(entities = [MovieReviewEntity::class, RemoteKeys::class], version = 1)
abstract class MovieReviewDataBase : RoomDatabase() {
    abstract fun getMovieReviewDao(): MovieReviewDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remote_keys WHERE repoId = :repoId")
    suspend fun remoteKeysRepoId(repoId: Int): RemoteKeys?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val repoId: Int,
    val prevKey: Int?,
    val nextKey: Int?,
    var hasMore: Boolean?
)


@Dao
interface MovieReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMovieReviews(movieReviewEntity: List<MovieReviewEntity>)

    @Query("SELECT * from movieReviewEntity")
    fun moviesPagingSource(): PagingSource<Int, MovieReviewEntity>

    @Query("SELECT * FROM movieReviewEntity WHERE uId = :entityId")
    suspend fun movieEntityId(entityId: Int): MovieReviewEntity?

    @Query("SELECT COUNT(title) FROM movieReviewEntity")
    suspend fun getRowCount(): Int = 0

    @Query("DELETE FROM movieReviewEntity")
    suspend fun clearAllData()
}

@Entity(tableName = "movieReviewEntity")
data class MovieReviewEntity(
    @PrimaryKey val uId: Int = 0,

    @ColumnInfo(name = "title")
    @SerializedName("title")
    var title: String = "",

    @ColumnInfo(name = "review_by")
    @SerializedName("review_by")
    var review_by: String = "",

    @ColumnInfo(name = "headline")
    @SerializedName("headline")
    var headline: String = "",

    @ColumnInfo(name = "summary_short")
    @SerializedName("summary_short")
    var summary_short: String = "",

    @ColumnInfo(name = "image_url")
    @SerializedName("image_url")
    var image_url: String = "",

    @ColumnInfo(name = "external_link")
    @SerializedName("external_link")
    var external_link: String = "",

    @ColumnInfo(name = "opening_date")
    @SerializedName("opening_date")
    var opening_date: String = "",

    @ColumnInfo(name = "page")
    @SerializedName("page")
    var page: Int = 0,

    @ColumnInfo(name = "time")
    @SerializedName("time")
    var time: Long = 0,

    @ColumnInfo(name = "has_more")
    @SerializedName("has_more")
    var hasMore: Boolean = false
)

fun Result.toEntityData(pageOffset: Int, currentTime: Long, hasMore: Boolean, uId: Int) = MovieReviewEntity(
    title = this.display_title.orEmpty(),
    review_by = this.byline.orEmpty(),
    headline = this.headline.orEmpty(),
    summary_short = this.summary_short.orEmpty(),
    external_link = this.link?.url.orEmpty(),
    image_url = this.multimedia?.src.orEmpty(),
    opening_date = this.opening_date.orEmpty(),
    page = pageOffset,
    time = currentTime,
    hasMore = hasMore,
    uId = uId
)
