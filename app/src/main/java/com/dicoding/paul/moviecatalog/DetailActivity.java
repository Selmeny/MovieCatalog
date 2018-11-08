package com.dicoding.paul.moviecatalog;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dicoding.paul.moviecatalog.entity.FavouriteItems;
import com.dicoding.paul.moviecatalog.nowplayingfragment.NowPlayingItems;
import com.dicoding.paul.moviecatalog.searchmovie.MovieItems;
import com.dicoding.paul.moviecatalog.upcomingfragment.UpcomingItems;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dicoding.paul.moviecatalog.database.FavouriteContract.CONTENT_URI;
import static com.dicoding.paul.moviecatalog.database.FavouriteContract.Notecolumns.ORIGINAL_TITLE;
import static com.dicoding.paul.moviecatalog.database.FavouriteContract.Notecolumns.OVERVIEW;
import static com.dicoding.paul.moviecatalog.database.FavouriteContract.Notecolumns.POSTER;
import static com.dicoding.paul.moviecatalog.database.FavouriteContract.Notecolumns.RELEASE_DATE;
import static com.dicoding.paul.moviecatalog.database.FavouriteContract.Notecolumns.SCORE;
import static com.dicoding.paul.moviecatalog.widget.ImageBannerWidget.widgetRemoteUpdate;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener {
    private String URL;
    private FavouriteItems favouriteItems;
    public static final String EXTRA_MOVIES = "extra_movies";
    public static final String EXTRA_NOW_PLAYING = "extra_now_playing";
    public static final String EXTRA_UPCOMING = "extra_upcoming";
    public boolean liked = false;

    @BindView(R.id.tb_my_toolbar) Toolbar toolbar;
    @BindView(R.id.img_poster_detail) ImageView poster;
    @BindView(R.id.tv_original_title_detail) TextView originalTitle;
    @BindView(R.id.tv_release_date_detail) TextView releaseDate;
    @BindView(R.id.tv_score_detail) TextView score;
    @BindView(R.id.tv_overview_detail) TextView overView;
    @BindView(R.id.btn_favourite) ImageButton imageButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        //Set this method to display toolbar's back button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        //Create an instance of items to get the data sent through intent
        MovieItems movieItems = getIntent().getParcelableExtra(EXTRA_MOVIES);
        NowPlayingItems nowPlayingItems = getIntent().getParcelableExtra(EXTRA_NOW_PLAYING);
        UpcomingItems upcomingItems = getIntent().getParcelableExtra(EXTRA_UPCOMING);

        Uri uri = getIntent().getData();

        if (movieItems != null)  {
            URL = movieItems.getPoster();
            Glide.with(getApplicationContext())
                    .load(URL)
                    .into(poster);

            originalTitle.setText(movieItems.getOriginalTitle());
            releaseDate.setText(movieItems.getReleaseDate());
            overView.setText(movieItems.getOverview());
            score.setText(movieItems.getScore());

        } else if (nowPlayingItems != null) {
            URL = nowPlayingItems.getPoster();
            Glide.with(getApplicationContext())
                    .load(URL)
                    .into(poster);

            originalTitle.setText(nowPlayingItems.getOriginalTitle());
            releaseDate.setText(nowPlayingItems.getReleaseDate());
            overView.setText(nowPlayingItems.getOverview());
            score.setText(nowPlayingItems.getScore());

        }  else if (upcomingItems != null) {
            URL = upcomingItems.getPoster();
            Glide.with(getApplicationContext())
                    .load(URL)
                    .into(poster);

            originalTitle.setText(upcomingItems.getOriginalTitle());
            releaseDate.setText(upcomingItems.getReleaseDate());
            overView.setText(upcomingItems.getOverview());
            score.setText(upcomingItems.getScore());

        } else if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null,
                    null);

            if (cursor != null) {
                if (cursor.moveToFirst()) favouriteItems = new FavouriteItems(cursor);
                cursor.close();

                URL = favouriteItems.getPoster();
                Glide.with(getApplicationContext())
                        .load(URL)
                        .into(poster);

                originalTitle.setText(favouriteItems.getOriginalTitle());
                releaseDate.setText(favouriteItems.getReleaseDate());
                overView.setText(favouriteItems.getOverview());
                score.setText(favouriteItems.getScore());
            }
        }

        if (uri != null) {
            imageButton.setVisibility(View.GONE);
        } else {
            imageButton.setVisibility(View.VISIBLE);
            imageButton.setOnClickListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.share:
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, URL);
                startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share_using)));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Add image button and set a listener on it
    //It will be used for adding a movie to favourite list
    @Override
    public void onClick(View v) {
        Drawable btnLiked = getResources().getDrawable(R.drawable.ic_favorite_black_24dp);

        String url = URL;
        String title =  originalTitle.getText().toString().trim();
        String date = releaseDate.getText().toString().trim();
        String ov = overView.getText().toString().trim();
        String sc = score.getText().toString().trim();

        if (v.getId() == R.id.btn_favourite) {
            if (!liked) {
                liked = true;
                imageButton.setBackground(btnLiked);

                ContentValues values = new ContentValues();
                values.put(POSTER, url);
                values.put(ORIGINAL_TITLE, title);
                values.put(RELEASE_DATE, date);
                values.put(OVERVIEW, ov);
                values.put(SCORE, sc);

                getContentResolver().insert(CONTENT_URI, values);
                showSnackbarMessage(getResources().getString(R.string.movie_added));

                //Update widget
                widgetRemoteUpdate(getApplicationContext());
            }
        }
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(imageButton, message, Snackbar.LENGTH_SHORT).show();
    }
}
