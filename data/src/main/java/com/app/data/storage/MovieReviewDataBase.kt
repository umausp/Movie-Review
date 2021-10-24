package com.app.data.storage

import androidx.room.*
import com.app.data.model.Result
import com.google.gson.annotations.SerializedName

@Database(entities = [MovieReviewEntity::class], version = 1)
abstract class MovieReviewDataBase : RoomDatabase() {
    abstract fun getMovieReviewDao(): MovieReviewDao
}

@Dao
interface MovieReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMovieReviews(movieReviewEntity: List<MovieReviewEntity>)

    @Query("select * from movieReviewEntity where page =:page")
    suspend fun getAllSavedList(page: Int): List<MovieReviewEntity>

    @Query("DELETE FROM movieReviewEntity")
    suspend fun clearAllData()
}

@Entity(tableName = "movieReviewEntity")
data class MovieReviewEntity(
    @PrimaryKey(autoGenerate = true) val uId: Int = 0,

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
    var time: Long = 0
)

fun Result.toEntityData(pageOffset: Int, currentTime: Long) = MovieReviewEntity(
    title = this.display_title.orEmpty(),
    review_by = this.byline.orEmpty(),
    headline = this.headline.orEmpty(),
    summary_short = this.summary_short.orEmpty(),
    external_link = this.link?.url.orEmpty(),
    image_url = this.multimedia?.src.orEmpty(),
    opening_date = this.opening_date.orEmpty(),
    page = pageOffset,
    time = currentTime
)