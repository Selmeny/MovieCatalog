package com.dicoding.paul.moviecatalog.searchmovie;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.dicoding.paul.moviecatalog.BuildConfig;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MyAsyncTaskLoader extends AsyncTaskLoader<ArrayList<MovieItems>> {
    private ArrayList<MovieItems> movieList;
    private boolean mHasResult = false;
    private String mMovieName;
    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String URL_MAIN = "https://api.themoviedb.org/3/search/movie?api_key=";
    private static final String URL_QUERY = "&language=en-US&query=";
    private String TAG = MyAsyncTaskLoader.class.getSimpleName();

    public MyAsyncTaskLoader(final Context context, String movieName) {
        super(context);

        onContentChanged();
        this.mMovieName = movieName;
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
        else if (mHasResult)
            deliverResult(movieList);
    }

    @Override
    public void deliverResult(final ArrayList<MovieItems> data) {
        movieList = data;
        mHasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mHasResult) {
            onReleaseResources(movieList);
            movieList = null;
            mHasResult = false;
        }
    }

    @Override
    public ArrayList<MovieItems> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();

        final ArrayList<MovieItems> movieItemList = new ArrayList<>();

        String url = URL_MAIN + API_KEY + URL_QUERY + mMovieName;

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
                Log.d(TAG, "onStart is called");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i=0; i<list.length(); i++) {
                      JSONObject movie = list.getJSONObject(i);
                      MovieItems movieItems = new MovieItems(movie);
                      movieItemList.add(movieItems);

                      Log.d(TAG, "ArrayList has been loaded");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "Loading failed");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.e(TAG, "Loading failed");
            }

        });

        return movieItemList;
    }

    //ArrayList will be cleared if this method is called
    private void onReleaseResources(ArrayList<MovieItems> data) {
        movieList = data;
        movieList.clear();
        Log.d(TAG, "ArrayList is cleared");
    }
}
