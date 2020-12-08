package com.moviestask.features.movies

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.domain.models.ResultDO
import com.like.LikeButton
import com.like.OnLikeListener
import com.moviestask.R
import com.squareup.picasso.Picasso
import java.io.Serializable
import kotlin.collections.ArrayList


class SearchMovieViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    private val tvTitle by lazy { view.findViewById<TextView>(R.id.tv_movie_name) }
    private val tvVoteAverage by lazy { view.findViewById<TextView>(R.id.tv_vote_average) }
    private val image by lazy { view.findViewById<ImageView>(R.id.iv_movie_banner) }
    private val favBtn by lazy { view.findViewById<LikeButton>(R.id.fav_btn) }
    fun bind(movie: ResultDO) {
        tvTitle.text = movie.title ?: ""
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
                movie.isFav=true
            }

            override fun unLiked(likeButton: LikeButton) {
                Intent(ACTION_REMOVE_MOVIE_FROM_FAV)
                    .putExtra(EXTRA_FAV_MOVIE, movie as Serializable)
                    .also { view.context.sendBroadcast(it) }
                movie.isFav=false
            }
        })
    }

}

class SearchListMoviesAdapter(

    private val moviesResult:List<ResultDO>
) : RecyclerView.Adapter<SearchMovieViewHolder>() ,Filterable{
var contactListFiltered:List<ResultDO> =ArrayList<ResultDO>()
    init {
        this.contactListFiltered=moviesResult

    }

    override fun onCreateViewHolder(parentView: ViewGroup, p1: Int): SearchMovieViewHolder {
        return LayoutInflater
            .from(parentView.context)
            .inflate(R.layout.movie_list_item, parentView, false)
            .let { SearchMovieViewHolder(it) }
    }

    override fun onBindViewHolder(viewHolder: SearchMovieViewHolder, position: Int) {
        viewHolder.bind(contactListFiltered[position])
    }

    override fun getItemCount() = contactListFiltered.size
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    contactListFiltered = moviesResult
                } else {
                    val filteredList =
                        ArrayList<ResultDO>()
                    for (row in contactListFiltered) {

                        if (row.originalTitle?.toLowerCase()?.contains(charString.toLowerCase())!!||row.releaseDate?.toLowerCase()?.contains(charString.toLowerCase())!!) {
                            filteredList.add(row)
                        }
                    }
                    contactListFiltered = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = contactListFiltered
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                contactListFiltered = filterResults.values as ArrayList<ResultDO>
                notifyDataSetChanged()

            }
        }
    }

}

