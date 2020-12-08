package com.moviestask.features.movies;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.domain.models.ResultDO;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.moviestask.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.moviestask.features.movies.PagedListMoviesAdapterKt.ACTION_ADD_MOVIE_TO_FAV;
import static com.moviestask.features.movies.PagedListMoviesAdapterKt.ACTION_MOVIE_ITEM_CLICKED;
import static com.moviestask.features.movies.PagedListMoviesAdapterKt.ACTION_REMOVE_MOVIE_FROM_FAV;
import static com.moviestask.features.movies.PagedListMoviesAdapterKt.EXTRA_FAV_MOVIE;
import static com.moviestask.features.movies.PagedListMoviesAdapterKt.EXTRA_MOVIE;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder>
        implements Filterable {

    private List<ResultDO> movieList;
    private List<ResultDO> movieListFiltered;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvVoteAverage;
        public ImageView ivBanner;
        public LikeButton favBtn;

        public MyViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_movie_name);
            tvVoteAverage = view.findViewById(R.id.tv_vote_average);
            ivBanner = view.findViewById(R.id.iv_movie_banner);
            favBtn = view.findViewById(R.id.fav_btn);


        }
    }


    public MovieAdapter(List<ResultDO> list) {

        this.movieList = list;
        this.movieListFiltered = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ResultDO movie = movieListFiltered.get(position);
        holder.tvName.setText(movie.getOriginalTitle());
        holder.tvVoteAverage.setText(String.valueOf(movie.getVoteAverage()));
        holder.favBtn.setLiked(movie.isFav());
        String posterImage = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
        Picasso.with(holder.itemView.getContext()).load(posterImage)
                .into(holder.ivBanner);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(ACTION_MOVIE_ITEM_CLICKED);
            intent.putExtra(EXTRA_MOVIE, movie);
            holder.itemView.getContext().sendBroadcast(intent);
        });
        holder.favBtn.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Intent intent = new Intent(ACTION_ADD_MOVIE_TO_FAV);
                intent.putExtra(EXTRA_FAV_MOVIE, movie);
                holder.itemView.getContext().sendBroadcast(intent);
                movie.setFav(true);
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Intent intent = new Intent(ACTION_REMOVE_MOVIE_FROM_FAV);
                intent.putExtra(EXTRA_FAV_MOVIE, movie);
                holder.itemView.getContext().sendBroadcast(intent);
                movie.setFav(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    movieListFiltered = movieList;
                } else {
                    List<ResultDO> filteredList = new ArrayList<>();
                    for (ResultDO row : movieList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getOriginalTitle().toLowerCase().contains(charString.toLowerCase()) || row.getReleaseDate().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    movieListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = movieListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                movieListFiltered = (ArrayList<ResultDO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


}
