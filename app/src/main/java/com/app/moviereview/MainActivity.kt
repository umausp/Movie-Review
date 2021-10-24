package com.app.moviereview

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.app.moviereview.viewmodel.MovieReviewMainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    val viewModel: MovieReviewMainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        with(viewModel) {
            movieReviewResourceLiveData.observe(this@MainActivity) {
                it.onLoading {

                }
                it.onSuccess {
                    Toast.makeText(this@MainActivity, it.status, Toast.LENGTH_SHORT).show()
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
        val contextView = findViewById<View>(R.id.main)
        Snackbar.make(contextView, message, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.retry)) {
                retry.invoke()
            }.show()
    }
}
