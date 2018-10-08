package com.dicoding.paul.moviecatalog;

import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.support.v7.widget.SearchView;

import com.dicoding.paul.moviecatalog.SearchMovie.MovieAdapter;
import com.dicoding.paul.moviecatalog.SearchMovie.MovieItems;
import com.dicoding.paul.moviecatalog.SearchMovie.MyAsyncTaskLoader;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class  SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<MovieItems>>{
    private MovieAdapter movieAdapter;
    private SearchView searchView;
    private String movie;
    public static final String EXTRA_MOVIE = "extra_movie";

    @BindView(R.id.tb_my_toolbar) Toolbar toolbar;
    @BindView(R.id.progress_bar3) ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        //Set this method to display toolbar's back button
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        progressBar.setVisibility(View.VISIBLE);

        Bundle bundle = getIntent().getBundleExtra("movie");
        getSupportLoaderManager().initLoader(2, bundle,this);

        showRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search);

        searchView = (SearchView) searchItem.getActionView();
        searchView.onActionViewExpanded();
        searchView.clearFocus();
        searchView.setQueryHint(getResources().getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movie = searchView.getQuery().toString();
                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_MOVIE, movie);
                getSupportLoaderManager().restartLoader(2, bundle, SearchActivity.this);

                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
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

    @NonNull
    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int id, Bundle args) {
        String movieName = "";
        if (args != null) {
            movieName = args.getString(EXTRA_MOVIE);
        }
        return new MyAsyncTaskLoader(this, movieName);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<MovieItems>> loader, ArrayList<MovieItems> data) {
        movieAdapter.setMovieList(data);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<MovieItems>> loader) {
        movieAdapter.setMovieList(null);
        progressBar.setVisibility(View.GONE);
    }

    private void showRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.rv_movie_catalog);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        movieAdapter = new MovieAdapter(this);
        movieAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(movieAdapter);
        recyclerView.setHasFixedSize(true);
    }
}
