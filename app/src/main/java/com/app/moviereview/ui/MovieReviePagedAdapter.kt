package com.app.moviereview.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.data.model.MovieReviewItemModel
import com.app.data.storage.MovieReviewEntity
import com.app.moviereview.databinding.ItemMovieReviewLayoutBinding
import com.app.moviereview.utils.bindImageUrlWithImage
import javax.inject.Inject

class MovieReviePagedAdapter @Inject constructor() : PagingDataAdapter<MovieReviewEntity, MovieReviePagedAdapter.EpisodeViewHolder>(EpisodeComparator) {
    val movieReviewItemClick = MutableLiveData<MovieReviewEntity>()

    var onMovieItemClickListener : MovieItemClickListener?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EpisodeViewHolder(
            ItemMovieReviewLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        getItem(holder.absoluteAdapterPosition)?.let { holder.bind(it) }
    }

    inner class EpisodeViewHolder(private val binding: ItemMovieReviewLayoutBinding) :
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