package com.app.moviereview.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.data.model.MovieReviewItemModel
import com.app.moviereview.databinding.ItemMovieReviewLayoutBinding
import com.app.moviereview.utils.bindImageUrlWithImage
import javax.inject.Inject

class MovieReviewAdapter @Inject constructor() : RecyclerView.Adapter<MovieReviewAdapter.MovieReviewViewHolder>() {

    private var movies = ArrayList<MovieReviewItemModel>()
    val movieReviewItemClick = MutableLiveData<MovieReviewItemModel>()

    class MovieReviewViewHolder(val binding: ItemMovieReviewLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<MovieReviewItemModel>() {

            override fun areItemsTheSame(oldItem: MovieReviewItemModel, newItem: MovieReviewItemModel): Boolean =
                oldItem.movieReviewEntity.time == newItem.movieReviewEntity.time

            override fun areContentsTheSame(oldItem: MovieReviewItemModel, newItem: MovieReviewItemModel): Boolean =
                oldItem.equals(newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieReviewViewHolder {
        return MovieReviewViewHolder(ItemMovieReviewLayoutBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: MovieReviewViewHolder, position: Int) {
        holder.binding.apply {
            val imgUrl = movies[holder.absoluteAdapterPosition].movieReviewEntity.image_url
            model = movies[holder.absoluteAdapterPosition]
            imgPlaceholder.bindImageUrlWithImage(imgUrl)
            executePendingBindings()
        }.root.run {
            setOnClickListener {
                movieReviewItemClick.postValue(movies[holder.absoluteAdapterPosition])
            }
        }

    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setItems(movieReviews: ArrayList<MovieReviewItemModel>) {
        this.movies = movieReviews
        notifyItemRangeChanged(0, movies.size - 1)
    }

    fun addMoreItems(movieReviews: ArrayList<MovieReviewItemModel>) {
        val lastIndex = movies.size - 1
        this.movies.addAll(movieReviews)
        notifyItemRangeChanged(lastIndex, movies.size - 1)
    }
}