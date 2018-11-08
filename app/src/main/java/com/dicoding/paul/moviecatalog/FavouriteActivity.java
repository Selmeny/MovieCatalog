package com.dicoding.paul.moviecatalog;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.dicoding.paul.moviecatalog.favouritemovie.FavouriteAdapter;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dicoding.paul.moviecatalog.database.FavouriteContract.CONTENT_URI;

public class FavouriteActivity extends AppCompatActivity {
    private FavouriteAdapter favouriteAdapter;

    @BindView(R.id.progress_bar4) ProgressBar progressBar;
    @BindView(R.id.rv_favourite_movie) RecyclerView recyclerView;
    @BindView(R.id.tb_my_toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        progressBar.setVisibility(View.VISIBLE);

        new LoadFavouriteAsync().execute();

        showRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class LoadFavouriteAsync extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return getContentResolver().query(CONTENT_URI, null, null, null,
                    null);
        }

        @Override
        protected void onPostExecute(Cursor movies) {
            super.onPostExecute(movies);
            progressBar.setVisibility(View.GONE);

            favouriteAdapter.setFavouriteMovies(movies);
            favouriteAdapter.notifyDataSetChanged();

            if (movies.getCount() == 0) {
                showSnackbarMessage(getResources().getString(R.string.add_movie));
            }
        }
    }

    public void showRecyclerView() {
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        favouriteAdapter = new FavouriteAdapter(this);
        favouriteAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(favouriteAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    protected  void onDestroy() {
        super.onDestroy();
    }
}
