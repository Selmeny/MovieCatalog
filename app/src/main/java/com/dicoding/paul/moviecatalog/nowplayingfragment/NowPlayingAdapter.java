package com.dicoding.paul.moviecatalog.nowplayingfragment;

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

import butterknife.BindView;
import butterknife.ButterKnife;

//Use RecyclerView as a best practice and for better handling/performance
public class NowPlayingAdapter extends RecyclerView.Adapter<NowPlayingAdapter.MovieViewHolder> {
    private ArrayList<NowPlayingItems> nowPlayingList = new ArrayList<>();
    private Context context;

    NowPlayingAdapter(Context context) {
        this.context = context;
    }

    private ArrayList<NowPlayingItems> getNowPlayingList() {
        return nowPlayingList;
    }

    void setNowPlayingList(ArrayList<NowPlayingItems> nowPlayingList) {
        this.nowPlayingList = nowPlayingList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_now_playing, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        NowPlayingItems nowPlayingItems = getNowPlayingList().get(position);

        Glide.with(context)
                .load(nowPlayingItems.getPoster())
                .into(holder.imgPoster);
        holder.tvOriginalTitle.setText(nowPlayingItems.getOriginalTitle());
        holder.tvOverview.setText(nowPlayingItems.getOverview());
        holder.tvReleaseDate.setText(nowPlayingItems.getReleaseDate());
    }

    @Override
    public int getItemCount() {
        return getNowPlayingList().size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.img_poster) ImageView imgPoster;
        @BindView(R.id.tv_original_title) TextView tvOriginalTitle;
        @BindView(R.id.tv_release_date) TextView tvReleaseDate;
        @BindView(R.id.tv_overview) TextView tvOverview;
        @BindView(R.id.btn_detail) Button btnDetail;

        MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btnDetail.setOnClickListener(this);
        }

        //Make an intent to bring data from viewHolder to detail activity based on it's position
        @Override
        public void onClick(View v) {
            NowPlayingItems nowPlayingItems = getNowPlayingList().get(getAdapterPosition());
            Intent intent = new Intent(context, DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_NOW_PLAYING, nowPlayingItems);
            context.startActivity(intent);
        }
    }
}
