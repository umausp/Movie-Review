package com.app.moviereview

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.moviereview.databinding.ActivityMainBinding
import com.app.moviereview.viewmodel.MovieReviewMainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val viewModel: MovieReviewMainViewModel by viewModels()

    var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            model = viewModel
            lifecycleOwner = this@MainActivity
        }
        setContentView(binding?.root)

        with(viewModel) {
            movieReviewResourceLiveData.observe(this@MainActivity) {
                it.onLoading {
                    showLoadingToUI.value = it.isLoading()
                }

                it.onSuccess {
                    Toast.makeText(this@MainActivity, it.size.toString(), Toast.LENGTH_SHORT).show()
                }

                it.onNetworkError { retry ->
                    showSnackbar(getString(R.string.no_internet), retry)
                }

                it.onError { _, retry ->
                    showSnackbar(getString(R.string.something_went_wrong), retry)
                }
            }
        }
    }

    private fun showSnackbar(message: String, retry: () -> Unit) {
        Snackbar.make(binding?.main ?: return, message, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.retry)) {
                retry.invoke()
            }.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
