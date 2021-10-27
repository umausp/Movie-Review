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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.data.storage.MovieReviewEntity
import com.app.data.utils.log
import com.app.moviereview.R
import com.app.moviereview.databinding.FragmentMovieListLayoutBinding
import com.app.moviereview.viewmodel.MovieReviewMainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


@AndroidEntryPoint
class MovieReviewListFragment : Fragment(), MovieReviePagedAdapter.MovieItemClickListener {

    private var binding: FragmentMovieListLayoutBinding? = null
    private val viewModel: MovieReviewMainViewModel by activityViewModels()

    @Inject
    lateinit var movieReviewAdapter: MovieReviePagedAdapter

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
        with(viewModel) {
            launchOnLifecycleScope {
                movieReviewFlow.collectLatest {
                    movieReviewAdapter.submitData(it)
                }
            }
        }
        initView()
    }

    private fun initView() {
        binding?.rvMovieReview?.adapter = movieReviewAdapter
        binding?.rvMovieReview?.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.VERTICAL,
            false
        )
        movieReviewAdapter.onMovieItemClickListener = this
        binding?.rvMovieReview?.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            queryHint = getString(R.string.filter_by_name)
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    onFilterTextSubmit(query?.takeIf {
                        it.length > 2
                    })
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    onFilterQueryChange(newText?.takeIf {
                        it.length > 2
                    })
                    return true
                }

            })
        }
    }

    private fun onFilterTextSubmit(query: String?) {
        log(query)
    }

    private fun onFilterQueryChange(newText: String?) {
        log(newText)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_sort -> {
                sortByName()
                true
            }
            R.id.search -> {
                filterData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun filterData() {

    }

    private fun sortByName() {

    }

    private fun showSnackbar(message: String, retry: () -> Unit) {
        Snackbar.make(binding?.lytMainContainer ?: return, message, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.retry)) {
                retry.invoke()
            }.show()
    }

    private fun launchOnLifecycleScope(execute: suspend () -> Unit) {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
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
