package com.moviestask.features.movies

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.paging.PagedList
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.domain.models.ResultDO
import com.moviestask.R
import com.moviestask.di.DaggerAppComponent
import com.moviestask.di.ViewModelFactoryClean
import kotlinx.android.synthetic.main.fragment_list_movies.view.*
import javax.inject.Inject


class ListMoviesFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactoryClean
    private lateinit var viewModel: MoviesViewModel
    private lateinit var viewOfLayout: View
    private lateinit var searchView: SearchView
    private lateinit var adapter: MovieAdapter
    var localList = ArrayList<ResultDO>()
    var originalList: List<ResultDO> = ArrayList<ResultDO>()
    private lateinit var pagedList: PagedList<ResultDO>
    private lateinit var pagedListAdapter: PagedListMoviesAdapter
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == ACTION_MOVIE_ITEM_CLICKED) {
                openMovieDetailsScreen(intent.extras?.getSerializable(EXTRA_MOVIE) as ResultDO)
            }
            if (intent?.action == ACTION_ADD_MOVIE_TO_FAV) {
                viewModel.addMovieToFavoriteList(intent.extras?.getSerializable(EXTRA_FAV_MOVIE) as ResultDO)
            }
            if (intent?.action == ACTION_REMOVE_MOVIE_FROM_FAV) {
                viewModel.removeMovieToFavoriteList(intent.extras?.getSerializable(EXTRA_FAV_MOVIE) as ResultDO)
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
        activity?.registerReceiver(receiver, IntentFilter(ACTION_MOVIE_ITEM_CLICKED))
        activity?.registerReceiver(receiver, IntentFilter(ACTION_ADD_MOVIE_TO_FAV))
        activity?.registerReceiver(receiver, IntentFilter(ACTION_REMOVE_MOVIE_FROM_FAV))
        viewOfLayout = inflater.inflate(R.layout.fragment_list_movies, container, false)
        initializePagedListMoviesRecycler()
        openFavScreen()
        searchMovies()
        return viewOfLayout
    }

    private fun initializePagedListMoviesRecycler() {
        StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        ).apply {
            viewOfLayout.rv_paged_list_movies.layoutManager = this
        }
        pagedListAdapter = PagedListMoviesAdapter()
        viewOfLayout.rv_paged_list_movies.adapter = pagedListAdapter
        initializeSearchListMoviesRecycler()
        viewModel.getMovies().observe(viewLifecycleOwner,
            Observer<PagedList<ResultDO>> { t ->
                pagedListAdapter.submitList(t)
                pagedList = t
                viewModel.getFavoriteMovies()
            })
        setSearchListData(originalList)
        viewModel.getList().observe(viewLifecycleOwner, Observer {
            originalList = originalList + it!!
            setSearchListData(originalList)
            Toast.makeText(activity, "Get" + it.size + " Movie", Toast.LENGTH_LONG).show()
        })

    }

    private fun setSearchListData(list: List<ResultDO>) {
        adapter = MovieAdapter(list)
        viewOfLayout.rv_search_list_movies.adapter = adapter
    }

    private fun initializeSearchListMoviesRecycler() {
        StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        ).apply {
            viewOfLayout.rv_search_list_movies.layoutManager = this
        }


    }

    private fun openMovieDetailsScreen(resultDO: ResultDO) {
        if (isAdded) {
            val navController =
                Navigation.findNavController(requireActivity(), R.id.nav_main_host_fragment)
            if (navController.currentDestination!!.id == R.id.listMoviesFragment)
                navController.navigate(
                    ListMoviesFragmentDirections.actionListMoviesFragmentToMovieDetailsFragment(
                        resultDO
                    )
                )
        }

    }

    private fun openFavScreen() {
        viewOfLayout.mtrl_btn_open_fav.setOnClickListener {
            val navController =
                Navigation.findNavController(requireActivity(), R.id.nav_main_host_fragment)
            if (navController.currentDestination!!.id == R.id.listMoviesFragment)
                navController.navigate(ListMoviesFragmentDirections.actionListMoviesFragmentToFavoritesFragment())
        }
    }

    private fun observeAddMovie() {
        viewModel.isAddedLiveData.observe(viewLifecycleOwner, Observer {
            if (it!!) {
                Toast.makeText(activity, "Add successfully", Toast.LENGTH_LONG).show()
                viewModel.getFavoriteMovies()
            } else
                Toast.makeText(activity, "Fail to add", Toast.LENGTH_LONG).show()
        })
    }

    private fun fillFavField(pagedList: PagedList<ResultDO>) {
        for (x in 0 until pagedList.size) {
            for (y in 0 until localList.size) {
                if (localList[y].id == pagedList[x]?.id) {
                    pagedList[x]?.isFav = true
                    pagedListAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun fillFavSearchField(searchList: List<ResultDO>) {
        for (x in searchList.indices) {
            for (y in 0 until localList.size) {
                if (localList[y].id == searchList[x].id) {
                    searchList[x].isFav = true
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun observeGetLocalMovie() {
        viewModel.allFavoriteMoviesLiveData.observe(viewLifecycleOwner, Observer {
            localList = it as ArrayList<ResultDO>
            fillFavField(pagedList)
            fillFavSearchField(originalList)
        })
    }

    private fun observeGetList() {
        viewModel.getList().observe(viewLifecycleOwner, Observer {
            originalList = originalList + it!!
            setSearchListData(it)
            Toast.makeText(activity, "Get" + it.size + " Movie", Toast.LENGTH_LONG).show()
        })
    }

    private fun observeRemoveMovie() {
        viewModel.isRemovedLiveData.observe(viewLifecycleOwner, Observer {
            if (it!!) {
                Toast.makeText(activity, "Remove successfully", Toast.LENGTH_LONG).show()
                viewModel.getFavoriteMovies()
            } else
                Toast.makeText(activity, "Fail to remove", Toast.LENGTH_LONG).show()
        })
    }


    private fun searchMovies() {
        viewOfLayout.country_search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    viewOfLayout.rv_paged_list_movies.visibility = View.VISIBLE
                    viewOfLayout.rv_search_list_movies.visibility = View.GONE
                } else {
                    viewOfLayout.rv_paged_list_movies.visibility = View.GONE
                    viewOfLayout.rv_search_list_movies.visibility = View.VISIBLE
                    adapter.filter.filter(newText)
                }
                return false
            }

        })

    }

    override fun onStart() {
        super.onStart()
        observeAddMovie()
        observeRemoveMovie()
        // observeGetList()
        observeGetLocalMovie()
    }
}