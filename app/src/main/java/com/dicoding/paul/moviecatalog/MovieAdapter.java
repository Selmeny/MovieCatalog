package com.dicoding.paul.moviecatalog;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

//Use RecyclerView as a best practice and for better handling/performance
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private ArrayList<MovieItems> movieList = new ArrayList<>();
    private Context context;

    MovieAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<MovieItems> getMovieList() {
        return movieList;
    }

    public void setMovieList(ArrayList<MovieItems> movieList) {
        this.movieList = movieList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieItems movieItems = getMovieList().get(position);

        Glide.with(context)
                .load(movieItems.getPoster())
                .into(holder.imgPoster);
        holder.tvOriginalTitle.setText(movieItems.getOriginalTitle());
        holder.tvOverview.setText(movieItems.getOverview());
        holder.tvReleaseDate.setText(movieItems.getReleaseDate());
    }

    @Override
    public int getItemCount() {
        return getMovieList().size();
    }

    //Set onClickListener on the viewHolder instead of specific button, because we don't have any
    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgPoster;
        TextView tvOriginalTitle, tvReleaseDate, tvOverview;
        public MovieViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imgPoster = itemView.findViewById(R.id.img_poster);
            tvOriginalTitle = itemView.findViewById(R.id.tv_original_title);
            tvOverview = itemView.findViewById(R.id.tv_overview);
            tvReleaseDate = itemView.findViewById(R.id.tv_release_date);
        }

        //Make an intent to bring data from viewHolder to detail activity based on it's position
        @Override
        public void onClick(View v) {
            MovieItems movieItems = getMovieList().get(getAdapterPosition());
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_MOVIES, movieItems);
            context.startActivity(intent);
        }
    }
}
