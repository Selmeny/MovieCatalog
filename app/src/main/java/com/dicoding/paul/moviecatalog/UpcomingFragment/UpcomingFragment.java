package com.dicoding.paul.moviecatalog.UpcomingFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dicoding.paul.moviecatalog.R;

import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpcomingFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<UpcomingItems>> {
    private UpcomingAdapter upcomingAdapter;
    private ProgressBar progressBar;

    public UpcomingFragment() {
        // Required empty public constructor
    }
    @Override

    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getActivity()).getSupportLoaderManager().initLoader(1, null, this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upcoming, container, false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar = Objects.requireNonNull(getActivity()).findViewById(R.id.progress_bar2);
        progressBar.setVisibility(View.VISIBLE);
        showRecyclerView();
    }

    @NonNull
    @Override
    public Loader<ArrayList<UpcomingItems>> onCreateLoader(int id, Bundle args) {
        return new MyAsyncTaskLoaderUpcoming(getContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<UpcomingItems>> loader, ArrayList<UpcomingItems> data) {
        upcomingAdapter.setUpcomingList(data);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        upcomingAdapter.setUpcomingList(null);
        progressBar.setVisibility(View.GONE);

    }
    private void showRecyclerView() {
        RecyclerView recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.rv_upcoming);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        upcomingAdapter = new UpcomingAdapter(getActivity());
        upcomingAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(upcomingAdapter);
        recyclerView.setHasFixedSize(true);
    }
}
