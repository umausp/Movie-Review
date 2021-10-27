package com.app.moviereview.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.data.storage.MovieReviewEntity
import com.app.moviereview.R
import com.app.moviereview.databinding.FragmentMovieListLayoutBinding
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
        observeListData()
        initView()
    }

    private fun observeListData() {
        launchOnLifecycleScope {
            viewModel.movieReviewFlow.collectLatest {
                movieReviewAdapter.submitData(it)
            }
        }
    }

    private fun initView() {
        binding?.rvMovieReview?.adapter = movieReviewAdapter
        binding?.rvMovieReview?.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL,
            false
        )
        movieReviewAdapter.onMovieItemClickListener = this
        launchOnLifecycleScope {
            movieReviewAdapter.loadStateFlow.collectLatest {
                viewModel.showLoadingToUI.value = it.refresh is LoadState.Loading
                showSnackbar(it.refresh is LoadState.Error)
            }
        }
        binding?.rvMovieReview?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        viewModel.showErrorUI.observe(viewLifecycleOwner){
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
                    onFilterText(query?.takeIf {
                        it.length > 2
                    })
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText?.length == 0) {
                        observeListData()
                    } else {
                        onFilterText(newText?.takeIf {
                            it.length > 2
                        })
                    }
                    return true
                }

            })
        }
    }

    private fun resetOriginalData(name: String) {
        launchOnLifecycleScope {
            viewModel.movieReviewFlow.collectLatest {
                movieReviewAdapter.submitData(it.filter { it.headline.contains(name, ignoreCase = true) })
            }
        }
    }

    private fun onFilterText(query: String?) {
        query?.let { resetOriginalData(it) }
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

    private fun sortByName() {
        launchOnLifecycleScope {
            //Short with Adapter data
            sortByNameWithData()
        }
    }

    private fun sortByNameWithData() {
        val adapterData = movieReviewAdapter.snapshot().items.sortedBy { it.title }
        movieReviewAdapter.submitData(lifecycle, PagingData.from(adapterData))
        activity?.runOnUiThread {
            binding?.rvMovieReview?.smoothScrollToPosition(0)
        }
    }

    private fun showSnackbar(isError: Boolean) {
        if (isError) {
            Snackbar.make(binding?.lytMainContainer ?: return, getString(R.string.something_went_wrong), Snackbar.LENGTH_INDEFINITE)
                .show()
        }
    }

    private fun launchOnLifecycleScope(execute: suspend () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            execute()
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
