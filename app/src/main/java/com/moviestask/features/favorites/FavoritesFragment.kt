package com.moviestask.features.favorites

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.domain.models.ResultDO
import com.moviestask.R
import com.moviestask.di.DaggerAppComponent
import com.moviestask.di.ViewModelFactoryClean
import com.moviestask.features.movies.MoviesViewModel
import kotlinx.android.synthetic.main.fragment_favorites.view.*
import javax.inject.Inject


class FavoritesFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactoryClean
    private lateinit var viewModel: MoviesViewModel
    private lateinit var viewOfLayout: View
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_FAV_MOVIE_ITEM_CLICKED) {
                openMovieDetailsScreen(intent.extras?.getSerializable(EXTRA_MOVIE_ITEM) as ResultDO)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DaggerAppComponent.builder().application(activity?.applicationContext as Application)
            ?.build()
            ?.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[MoviesViewModel::class.java]
        activity?.registerReceiver(receiver, IntentFilter(ACTION_FAV_MOVIE_ITEM_CLICKED))
        viewModel.getFavoriteMovies()
        viewOfLayout = inflater.inflate(R.layout.fragment_favorites, container, false)
        return viewOfLayout
    }

    private fun openMovieDetailsScreen(resultDO: ResultDO) {
        val navController =
            activity?.let { Navigation.findNavController(it, R.id.nav_main_host_fragment) }
        if (navController?.currentDestination!!.id == R.id.favoritesFragment)
            navController.navigate(
                FavoritesFragmentDirections.actionFavoritesFragmentToMovieDetailsFragment(
                    resultDO
                )
            )
    }


    private fun initializeMoviesRecycler() {
        viewOfLayout.rv_movies.layoutManager = LinearLayoutManager(activity)
        viewModel.allFavoriteMoviesLiveData.observe(viewLifecycleOwner,
            Observer {
                val liveData = MutableLiveData<List<ResultDO>>()
                liveData.postValue(it)
                val adapter = FavoritesAdapter(this, liveData)
                viewOfLayout.rv_movies.adapter = adapter
            })
    }

    override fun onStart() {
        super.onStart()
        initializeMoviesRecycler()
    }
}