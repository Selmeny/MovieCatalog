package com.dicoding.paul.moviecatalog;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    private String URL;
    public static final String EXTRA_MOVIES = "extra_movies";
    public static final String EXTRA_NOW_PLAYING = "extra_now_playing";
    public static final String EXTRA_UPCOMING = "extra_upcoming";

    @BindView(R.id.tb_my_toolbar) Toolbar toolbar;
    @BindView(R.id.img_poster_detail) ImageView poster;
    @BindView(R.id.tv_original_title_detail) TextView originalTitle;
    @BindView(R.id.tv_release_date_detail) TextView releaseDate;
    @BindView(R.id.tv_score_detail) TextView score;
    @BindView(R.id.tv_overview_detail) TextView overView;


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

        if (movieItems != null)  {
            URL = movieItems.getPoster();
            Glide.with(getApplicationContext())
                    .load(URL)
                    .into(poster);
            originalTitle.setText(movieItems.getOriginalTitle());
            releaseDate.setText(movieItems.getReleaseDate());
            score.setText(movieItems.getScore());
            overView.setText(movieItems.getOverview());
        } else if (nowPlayingItems != null) {
            URL = nowPlayingItems.getPoster();
            Glide.with(getApplicationContext())
                    .load(URL)
                    .into(poster);
            originalTitle.setText(nowPlayingItems.getOriginalTitle());
            releaseDate.setText(nowPlayingItems.getReleaseDate());
            score.setText(nowPlayingItems.getScore());
            overView.setText(nowPlayingItems.getOverview());
        }  else if (upcomingItems != null) {
            URL = upcomingItems.getPoster();
            Glide.with(getApplicationContext())
                    .load(URL)
                    .into(poster);
            originalTitle.setText(upcomingItems.getOriginalTitle());
            releaseDate.setText(upcomingItems.getReleaseDate());
            score.setText(upcomingItems.getScore());
            overView.setText(upcomingItems.getOverview());
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
}
