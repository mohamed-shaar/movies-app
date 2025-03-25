package com.company.moviesapp.listscreen.presentation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.company.moviesapp.BuildConfig
import com.company.moviesapp.detailsscreen.presentation.MovieDetailsActivity
import com.company.moviesapp.listscreen.presentation.viewmodel.MoviesViewModel
import com.company.moviesapp.shared.data.local.datasource.TokenProvider
import com.company.moviesapp.ui.theme.MoviesAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val moviesViewModel by viewModels<MoviesViewModel>()

    @Inject
    lateinit var tokenProvider: TokenProvider

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val secret = BuildConfig.MOVIES_API_KEY
        tokenProvider.setToken(secret)
        moviesViewModel.getData()
        setContent {
            MoviesAppTheme {
                Scaffold(modifier = Modifier.fillMaxWidth()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        MovieList(
                            moviesViewModel = moviesViewModel,
                            onClick = { id -> // Create an Intent to navigate to SecondActivity
                                val intent =
                                    Intent(applicationContext, MovieDetailsActivity::class.java)

                                // Optionally, pass data to the next activity
                                intent.putExtra("id", id)

                                // Start the SecondActivity
                                startActivity(intent)
                            }, onSearch = { query ->
                                searchMovies(query)
                            }, onToggleWatchLater = { movieId, isAdded ->
                                moviesViewModel.toggleWatchLater(movieId, isAdded)
                            })
                    }
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        moviesViewModel.refreshMovies()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun searchMovies(it: String) {
        moviesViewModel.setSearchText(it)
    }
}