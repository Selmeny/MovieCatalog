package com.dicoding.paul.moviecatalog.nowplayingfragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.Fragment;
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
public class NowPlayingFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<NowPlayingItems>> {
    private NowPlayingAdapter nowPlayingAdapter;
    private ProgressBar progressBar;

    public NowPlayingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getActivity()).getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_now_playing, container, false);
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        progressBar = Objects.requireNonNull(getActivity()).findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        showRecyclerView();
    }

    @NonNull
    @Override
    public Loader<ArrayList<NowPlayingItems>> onCreateLoader(int id, Bundle args) {
        return new MyAsyncTaskLoaderNowPlaying(getContext());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<NowPlayingItems>> loader, ArrayList<NowPlayingItems> data) {
        nowPlayingAdapter.setNowPlayingList(data);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<NowPlayingItems>> loader) {
        nowPlayingAdapter.setNowPlayingList(null);
        progressBar.setVisibility(View.GONE);
    }

    private void showRecyclerView() {
        RecyclerView recyclerView = Objects.requireNonNull(getView()).findViewById(R.id.rv_now_playing);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        nowPlayingAdapter = new NowPlayingAdapter(getActivity());
        nowPlayingAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(nowPlayingAdapter);
        recyclerView.setHasFixedSize(true);
    }
}
