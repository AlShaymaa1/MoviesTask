package com.moviestask.features.moviedetails

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.domain.models.ResultDO
import com.like.LikeButton
import com.like.OnLikeListener
import com.moviestask.R
import com.moviestask.di.DaggerAppComponent
import com.moviestask.di.ViewModelFactoryClean
import com.moviestask.features.movies.MoviesViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_movie_details.view.*
import javax.inject.Inject

class MovieDetailsFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactoryClean
    private lateinit var viewModel: MoviesViewModel
    private lateinit var viewOfLayout: View
    private val args: MovieDetailsFragmentArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        DaggerAppComponent.builder().application(activity?.applicationContext as Application)
            ?.build()
            ?.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[MoviesViewModel::class.java]
        viewOfLayout= inflater.inflate(R.layout.fragment_movie_details, container, false)
        handleFavClick()
        return viewOfLayout
    }

    private fun setMovieDetails(resultDO: ResultDO) {
        viewOfLayout.tv_date.text =resultDO.releaseDate
        viewOfLayout.tv_desc.text =resultDO.overview
        viewOfLayout.tv_title.text = resultDO.originalTitle
        viewOfLayout.fav_btn.isLiked= resultDO.isFav!!
        val posterImage = "https://image.tmdb.org/t/p/w500"+resultDO.posterPath
        Picasso.with(activity).load(posterImage).into(viewOfLayout.iv_header)
    }

    private fun handleFavClick(){
        viewOfLayout.fav_btn.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton) {
                viewModel.addMovieToFavoriteList(args.movieDetails)
            }

            override fun unLiked(likeButton: LikeButton) {
                viewModel.removeMovieToFavoriteList(args.movieDetails)
            }
        })
    }
    private fun observeRemoveMovie() {
        viewModel.isRemovedLiveData.observe(viewLifecycleOwner, Observer {
            if (it!!)
                Toast.makeText(activity, "Remove successfully", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(activity, "Fail to remove", Toast.LENGTH_LONG).show()
        })
    }
    private fun observeAddMovie() {
        viewModel.isAddedLiveData.observe(viewLifecycleOwner, Observer {
            if (it!!)
                Toast.makeText(activity, "Add successfully", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(activity, "Fail to add", Toast.LENGTH_LONG).show()
        })
    }

    override fun onStart() {
        super.onStart()
        setMovieDetails(args.movieDetails)
        observeAddMovie()
        observeRemoveMovie()
    }
}