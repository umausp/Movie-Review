package com.app.moviereview.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.data.model.MovieReviewItemModel
import com.app.data.storage.MovieReviewEntity
import com.app.moviereview.databinding.ItemMovieReviewLayoutBinding
import com.app.moviereview.utils.bindImageUrlWithImage
import javax.inject.Inject

class MovieReviewPagedAdapter @Inject constructor() : PagingDataAdapter<MovieReviewEntity, MovieReviewPagedAdapter.MovieReviewItemViewHolder>(EpisodeComparator) {

    var onMovieItemClickListener: MovieItemClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MovieReviewItemViewHolder(
            ItemMovieReviewLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: MovieReviewItemViewHolder, position: Int) {
        getItem(holder.absoluteAdapterPosition)?.let { holder.bind(it) }
    }

    inner class MovieReviewItemViewHolder(private val binding: ItemMovieReviewLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                onMovieItemClickListener?.onMovieItemClicked(
                    getItem(absoluteAdapterPosition) as MovieReviewEntity
                )
            }
        }

        fun bind(item: MovieReviewEntity) = with(binding) {
            model = MovieReviewItemModel(item)
            val imgUrl = item.image_url
            imgPlaceholder.bindImageUrlWithImage(imgUrl)

        }
    }

    object EpisodeComparator : DiffUtil.ItemCallback<MovieReviewEntity>() {
        override fun areItemsTheSame(oldItem: MovieReviewEntity, newItem: MovieReviewEntity) =
            oldItem.uId == newItem.uId

        override fun areContentsTheSame(oldItem: MovieReviewEntity, newItem: MovieReviewEntity) =
            oldItem == newItem
    }

    interface MovieItemClickListener {
        fun onMovieItemClicked(entity: MovieReviewEntity)
    }
}