package com.moviestask.features.favorites

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.domain.models.ResultDO
import com.moviestask.R
import com.squareup.picasso.Picasso
import java.io.Serializable


const val ACTION_FAV_MOVIE_ITEM_CLICKED = "ACTION_FAV_MOVIE_ITEM_CLICKED"
const val EXTRA_MOVIE_ITEM = "EXTRA_MOVIE_ITEM"

class FavoritesViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val tvTitle by lazy { view.findViewById<TextView>(R.id.tv_movie_name) }
    private val tvVoteAverage by lazy { view.findViewById<TextView>(R.id.tv_vote_average) }
    private val image by lazy { view.findViewById<ImageView>(R.id.iv_movie_banner) }

    fun bind(movie: ResultDO) {

        tvTitle.text = movie.title ?: ""
        tvVoteAverage.text = movie.voteAverage.toString() ?: ""
        val posterImage = "https://image.tmdb.org/t/p/w500" + movie.posterPath
        Picasso.with(itemView.context).load(posterImage)
            .into(image)


        itemView.setOnClickListener {
            Intent(ACTION_FAV_MOVIE_ITEM_CLICKED)
                .putExtra(EXTRA_MOVIE_ITEM, movie as Serializable)
                .also { view.context.sendBroadcast(it) }
        }

    }

}

class FavoritesAdapter(
    lifecycleOwner: LifecycleOwner,
    private val moviesResult: MutableLiveData<List<ResultDO>>
) : RecyclerView.Adapter<FavoritesViewHolder>() {

    init {
        moviesResult.observe(lifecycleOwner, Observer {
            notifyDataSetChanged()
        })
    }

    override fun onCreateViewHolder(parentView: ViewGroup, p1: Int): FavoritesViewHolder {
        return LayoutInflater
            .from(parentView.context)
            .inflate(R.layout.fav_list_item, parentView, false)
            .let { FavoritesViewHolder(it) }
    }

    override fun onBindViewHolder(viewHolder: FavoritesViewHolder, position: Int) {
        viewHolder.bind(moviesResult.value!![position])
    }

    override fun getItemCount() = moviesResult.value?.size ?: 0

}

