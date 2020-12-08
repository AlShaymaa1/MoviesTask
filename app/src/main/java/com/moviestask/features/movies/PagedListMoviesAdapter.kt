package com.moviestask.features.movies

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.domain.models.ResultDO
import com.like.LikeButton
import com.like.OnLikeListener
import com.moviestask.R
import com.squareup.picasso.Picasso
import java.io.Serializable


const val ACTION_MOVIE_ITEM_CLICKED = "ACTION_MOVIE_ITEM_CLICKED"
const val EXTRA_MOVIE = "EXTRA_MOVIE"
const val ACTION_ADD_MOVIE_TO_FAV = "ACTION_ADD_MOVIE_TO_FAV"
const val ACTION_REMOVE_MOVIE_FROM_FAV = "ACTION_REMOVE_MOVIE_TO_FAV"
const val EXTRA_FAV_MOVIE = "EXTRA_FAV_MOVIE"

class PagedMovieViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val tvMovieName by lazy { view.findViewById<TextView>(R.id.tv_movie_name) }
    private val tvVoteAverage by lazy { view.findViewById<TextView>(R.id.tv_vote_average) }
    private val image by lazy { view.findViewById<ImageView>(R.id.iv_movie_banner) }
    private val favBtn by lazy { view.findViewById<LikeButton>(R.id.fav_btn) }

    fun bind(movie: ResultDO) {
        tvMovieName.text = movie.originalTitle ?: ""
        tvVoteAverage.text = movie.voteAverage.toString() ?: ""
        val posterImage = "https://image.tmdb.org/t/p/w500" + movie.posterPath
        favBtn.isLiked = movie.isFav!!
        Picasso.with(itemView.context).load(posterImage)
            .into(image)
        itemView.setOnClickListener {
            Intent(ACTION_MOVIE_ITEM_CLICKED)
                .putExtra(EXTRA_MOVIE, movie as Serializable)
                .also { view.context.sendBroadcast(it) }
        }
        favBtn.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton) {
                Intent(ACTION_ADD_MOVIE_TO_FAV)
                    .putExtra(EXTRA_FAV_MOVIE, movie as Serializable)
                    .also { view.context.sendBroadcast(it) }
                movie.isFav = true
            }

            override fun unLiked(likeButton: LikeButton) {
                Intent(ACTION_REMOVE_MOVIE_FROM_FAV)
                    .putExtra(EXTRA_FAV_MOVIE, movie as Serializable)
                    .also { view.context.sendBroadcast(it) }
                movie.isFav = false
            }
        })
    }

}

class PagedListMoviesAdapter() :
    PagedListAdapter<ResultDO, PagedMovieViewHolder>(
        DIFF_CALLBACK
    ), Filterable {

    lateinit var filtered: PagedList<ResultDO>
    override fun onBindViewHolder(holder: PagedMovieViewHolder, position: Int) {

        val movie: ResultDO? = getItem(position)

        movie?.let { holder.bind(it) }
    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<ResultDO>() {
            override fun areItemsTheSame(
                oldMovie: ResultDO,
                newMovie: ResultDO
            ) = oldMovie.id == newMovie.id

            override fun areContentsTheSame(
                oldMovie: ResultDO,
                newMovie: ResultDO
            ) = oldMovie == newMovie
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagedMovieViewHolder {
        return LayoutInflater
            .from(parent.context)
            .inflate(R.layout.movie_list_item, parent, false)
            .let { PagedMovieViewHolder(it) }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()

                if (charString.isEmpty()) {
                    filtered = currentList as PagedList<ResultDO>
                } else {
                    for (row in currentList!!) {
                        if (row.releaseDate != null && row.releaseDate!!.toLowerCase()
                                .contains(charString.toLowerCase())
                        ) {
                            filtered.add(row)
                        }
                        if (row.originalTitle != null && row.originalTitle!!.toLowerCase()
                                .contains(charString.toLowerCase())
                        ) {
                            if (row != null)
                                filtered.add(row)
                        }
                    }

                }
                val filterResults = FilterResults()
                filterResults.count = filtered.size ?: 0
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                if (filterResults.values != null)
                    submitList(filterResults.values as PagedList<ResultDO?>)
                notifyDataSetChanged()
            }
        }
    }

    // override fun getItemCount() = filtered?.size ?: 0

}