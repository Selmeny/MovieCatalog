package com.dicoding.paul.favouritemovies;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.dicoding.paul.favouritemovies.adapter.FavouriteMoviesAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.dicoding.paul.favouritemovies.database.FavouriteContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity {
    private FavouriteMoviesAdapter moviesAdapter;

    @BindView(R.id.tb_my_toolbar) Toolbar toolbar;
    @BindView(R.id.rv_favourite_movies) RecyclerView recyclerView;
    @BindView(R.id.pb_favourite_movies) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        progressBar.setVisibility(View.VISIBLE);

        new LoadFavouriteAsync().execute();

        showRecyclerView();
    }

    public class LoadFavouriteAsync extends AsyncTask<Void, Void, Cursor> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return getContentResolver().query(CONTENT_URI, null, null,
                    null, null);
        }

        @Override
        protected void onPostExecute(Cursor movies) {
            super.onPostExecute(movies);
            progressBar.setVisibility(View.GONE);

            moviesAdapter.setCursor(movies);
            moviesAdapter.notifyDataSetChanged();

            if (movies.getCount() == 0) {
                setSnackbarMessage(getResources().getString(R.string.no_data));
            }
        }
    }

    public void showRecyclerView() {
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        moviesAdapter = new FavouriteMoviesAdapter(this);
        moviesAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(moviesAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void setSnackbarMessage(String message) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).show();
    }
}
