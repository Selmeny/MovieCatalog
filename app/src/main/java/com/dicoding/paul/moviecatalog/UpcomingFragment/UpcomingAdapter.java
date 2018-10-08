package com.dicoding.paul.moviecatalog.UpcomingFragment;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dicoding.paul.moviecatalog.DetailActivity;
import com.dicoding.paul.moviecatalog.R;

import java.util.ArrayList;

//Use RecyclerView as a best practice and for better handling/performance
public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.MovieViewHolder> {
    private ArrayList<UpcomingItems> upcomingList = new ArrayList<>();
    private Context context;

    UpcomingAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<UpcomingItems> getUpcomingList() {
        return upcomingList;
    }

    public void setUpcomingList(ArrayList<UpcomingItems> upcomingList) {
        this.upcomingList = upcomingList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_upcoming, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        UpcomingItems upcomingItems = getUpcomingList().get(position);

        Glide.with(context)
                .load(upcomingItems.getPoster())
                .into(holder.imgPoster);
        holder.tvOriginalTitle.setText(upcomingItems.getOriginalTitle());
        holder.tvOverview.setText(upcomingItems.getOverview());
        holder.tvReleaseDate.setText(upcomingItems.getReleaseDate());
    }

    @Override
    public int getItemCount() {
        return getUpcomingList().size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imgPoster;
        TextView tvOriginalTitle, tvReleaseDate, tvOverview;
        Button btnDetail;
        public MovieViewHolder(View itemView) {
            super(itemView);
            imgPoster = itemView.findViewById(R.id.img_poster);
            tvOriginalTitle = itemView.findViewById(R.id.tv_original_title);
            tvOverview = itemView.findViewById(R.id.tv_overview);
            tvReleaseDate = itemView.findViewById(R.id.tv_release_date);
            btnDetail = itemView.findViewById(R.id.btn_detail);
            btnDetail.setOnClickListener(this);
        }

        //Make an intent to bring data from viewHolder to detail activity based on it's position
        @Override
        public void onClick(View v) {
            UpcomingItems upcomingItems = getUpcomingList().get(getAdapterPosition());
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_UPCOMING, upcomingItems);
            context.startActivity(intent);
        }
    }
}
