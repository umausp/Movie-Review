package com.app.moviereview.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.data.storage.MovieReviewEntity
import com.app.data.utils.launchOnLifecycleScope
import com.app.moviereview.R
import com.app.moviereview.databinding.FragmentMovieListLayoutBinding
import com.app.moviereview.utils.AppUtils
import com.app.moviereview.viewmodel.MovieReviewMainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


@AndroidEntryPoint
class MovieReviewListFragment : Fragment(), MovieReviewPagedAdapter.MovieItemClickListener {

    private var binding: FragmentMovieListLayoutBinding? = null
    private val viewModel: MovieReviewMainViewModel by activityViewModels()

    @Inject
    lateinit var appUtils: AppUtils

    @Inject
    lateinit var movieReviewAdapter: MovieReviewPagedAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMovieListLayoutBinding.inflate(inflater, container, false).apply {
            model = viewModel
            lifecycleOwner = this@MovieReviewListFragment
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initAdapter()
        observeListData()
    }

    private fun observeListData() {
        launchOnLifecycleScope(viewLifecycleOwner) {
            viewModel.movieReviewFlow.collectLatest {
                movieReviewAdapter.submitData(it)
            }
        }
    }

    private fun initAdapter() {
        movieReviewAdapter.onMovieItemClickListener = this
        launchOnLifecycleScope(viewLifecycleOwner) {
            movieReviewAdapter.loadStateFlow.collectLatest {
                viewModel.showLoadingToUI.value = it.refresh is LoadState.Loading
                showSnackbar(it.refresh is LoadState.Error)
            }
        }
    }

    private fun initView() {
        binding?.rvMovieReview?.adapter = movieReviewAdapter
        binding?.rvMovieReview?.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL,
            false
        )

        binding?.rvMovieReview?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        viewModel.showErrorUI.observe(viewLifecycleOwner) {
            it.also {
                showSnackbar(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            queryHint = getString(R.string.filter_by_name)
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    resetOriginalData(appUtils.canStartFiltering(query), query.orEmpty())
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    resetOriginalData(appUtils.canStartFiltering(newText), newText.orEmpty())
                    return true
                }

            })
        }
    }

    private fun resetOriginalData(canFilter: Boolean, name: String) {
        if (canFilter) {
            launchOnLifecycleScope(viewLifecycleOwner) {
                viewModel.movieReviewFlow.collectLatest { pagedData ->
                    val newData = pagedData.filter { it.title.startsWith(name, ignoreCase = true) }
                    movieReviewAdapter.submitData(newData)
                }
            }
        } else {
            observeListData()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_sort -> {
                sortByName()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Sort the item and scroll to 0th position
     */
    private fun sortByName() {
        launchOnLifecycleScope(viewLifecycleOwner) {
            val sortedMovieList: List<MovieReviewEntity> = appUtils.sortMovieEntity(movieReviewAdapter.snapshot().items)
            movieReviewAdapter.submitData(lifecycle, PagingData.from(sortedMovieList))
            binding?.rvMovieReview?.smoothScrollToPosition(0)
        }
    }

    private fun showSnackbar(isError: Boolean) {
        if (isError) {
            Snackbar.make(binding?.lytMainContainer ?: return, getString(R.string.something_went_wrong), Snackbar.LENGTH_INDEFINITE)
                .show()
        }
    }

    override fun onMovieItemClicked(entity: MovieReviewEntity) {
        viewModel.selectedReview.value = entity
        findNavController().navigate(R.id.action_MovieReviewListFragment_to_MovieReviewDetailsFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
