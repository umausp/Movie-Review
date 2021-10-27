package com.app.moviereview.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.app.moviereview.databinding.FragmentMovieReviewDetailsLayoutBinding
import com.app.moviereview.utils.bindImageUrlWithImage
import com.app.moviereview.viewmodel.MovieReviewMainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieReviewDetailsFragment : Fragment() {

    private var binding: FragmentMovieReviewDetailsLayoutBinding? = null
    private val viewModel: MovieReviewMainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMovieReviewDetailsLayoutBinding.inflate(inflater, container, false).apply {
            model = viewModel
            lifecycleOwner = this@MovieReviewDetailsFragment
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.imgPlaceholder?.bindImageUrlWithImage(viewModel.selectedReview.value?.image_url)
        binding?.btnSeeFullReview?.setOnClickListener {
            Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(viewModel.selectedReview.value?.external_link)
                startActivity(this)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
