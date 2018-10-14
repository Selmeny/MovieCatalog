package com.dicoding.paul.favouritemovies.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dicoding.paul.favouritemovies.DetailActivity;
import com.dicoding.paul.favouritemovies.MainActivity;
import com.dicoding.paul.favouritemovies.entity.FavouriteItems;
import com.dicoding.paul.favouritemovies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dicoding.paul.favouritemovies.database.FavouriteContract.CONTENT_URI;

//Use RecyclerView as a best practice and for better handling/performance
public class FavouriteMoviesAdapter extends RecyclerView.Adapter<FavouriteMoviesAdapter.FavouriteViewHolder> {
    private Cursor favouriteMovies;
    private Context context;

    public FavouriteMoviesAdapter(Context context) {
        this.context = context;
        notifyDataSetChanged();
    }

    public void setCursor(Cursor cursor) {
        this.favouriteMovies = cursor;
    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_favourite, parent, false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
        final FavouriteItems favouriteItems = getItem(position);

        Glide.with(context)
                .load(favouriteItems.getPoster())
                .into(holder.imgPoster);
        holder.tvOriginalTitle.setText(favouriteItems.getOriginalTitle());
        holder.tvReleaseDate.setText(favouriteItems.getReleaseDate());
        holder.tvOverview.setText(favouriteItems.getOverview());
    }

    @Override
    public int getItemCount() {
        if (favouriteMovies == null) return 0;
        return favouriteMovies.getCount();
    }

    private FavouriteItems getItem(int position) {
        if (!favouriteMovies.moveToPosition(position)) {
            throw new IllegalStateException("Position invalid");
        }
        return new FavouriteItems(favouriteMovies);
    }

    public class FavouriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.img_poster) ImageView imgPoster;
        @BindView(R.id.tv_original_title) TextView tvOriginalTitle;
        @BindView(R.id.tv_release_date) TextView tvReleaseDate;
        @BindView(R.id.tv_overview) TextView tvOverview;
        @BindView(R.id.btn_delete) Button btnDelete;
        @BindView(R.id.btn_detail) Button btnDetail;

        FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btnDelete.setOnClickListener(this);
            btnDetail.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            FavouriteItems favouriteItems = getItem(getAdapterPosition());

            switch (v.getId()) {
                //Make an intent to bring data from viewHolder to detail activity based on it's position
                case R.id.btn_detail:
                    Uri uri = Uri.parse(CONTENT_URI + "/" + favouriteItems.getId());
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.setData(uri);
                    context.startActivity(intent);
                    break;
                case R.id.btn_delete:
                    showAlertDialog(favouriteItems);
            }
        }
    }

    //Use this method to show confirmation dialog, delete a movie from database, and reload database
    private void showAlertDialog(final FavouriteItems favouriteItems) {
        String dialogTitle;
        String dialogMessage;

        dialogTitle = context.getResources().getString(R.string.delete_movie);
        dialogMessage = context.getResources().getString(R.string.delete_movie_sure);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            Uri uri = Uri.parse(CONTENT_URI + "/" + favouriteItems.getId());
                            context.getContentResolver().delete(uri, null,null);
                            ((MainActivity)context).new LoadFavouriteAsync().execute();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
