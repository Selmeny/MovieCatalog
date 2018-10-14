package com.dicoding.paul.favouritemovies;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dicoding.paul.favouritemovies.entity.FavouriteItems;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    private String URL;
    private FavouriteItems favouriteItems;

    @BindView(R.id.tb_my_toolbar) Toolbar toolbar;
    @BindView(R.id.img_poster_detail) ImageView poster;
    @BindView(R.id.tv_original_title_detail) TextView originalTitle;
    @BindView(R.id.tv_release_date_detail) TextView releaseDate;
    @BindView(R.id.tv_overview_detail) TextView overview;
    @BindView(R.id.tv_score_detail) TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        Uri uri = getIntent().getData();

        if (uri != null) {
            Cursor cursor = getContentResolver().query(uri, null, null,null,
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
                overview.setText(favouriteItems.getOverview());
                score.setText(favouriteItems.getScore());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
