package com.dicoding.paul.moviecatalog.upcomingfragment;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.dicoding.paul.moviecatalog.BuildConfig;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MyAsyncTaskLoaderUpcoming extends AsyncTaskLoader<ArrayList<UpcomingItems>> {
    private ArrayList<UpcomingItems> upcomingList;
    private boolean mHasResult = false;
    private static final String API_KEY = BuildConfig.API_KEY;
    private static final String URL_MAIN = "https://api.themoviedb.org/3/movie/upcoming?api_key=";
    private static final String URL_QUERY = "&language=en-US";
    private String TAG = MyAsyncTaskLoaderUpcoming.class.getSimpleName();

    public MyAsyncTaskLoaderUpcoming(final Context context) {
        super(context);
        onContentChanged();
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
        else if (mHasResult)
            deliverResult(upcomingList);
    }

    @Override
    public void deliverResult(final ArrayList<UpcomingItems> data) {
        upcomingList = data;
        mHasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mHasResult) {
            onReleaseResources(upcomingList);
            upcomingList = null;
            mHasResult = false;
        }
    }

    @Override
    public ArrayList<UpcomingItems> loadInBackground() {
        SyncHttpClient client = new SyncHttpClient();

        final ArrayList<UpcomingItems> upcomingItemsList = new ArrayList<>();

        String url = URL_MAIN + API_KEY + URL_QUERY;

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
                      UpcomingItems upcomingItems = new UpcomingItems(movie);
                      upcomingItemsList.add(upcomingItems);

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

        return upcomingItemsList;
    }

    //ArrayList will be cleared if this method is called
    private void onReleaseResources(ArrayList<UpcomingItems> data) {
        upcomingList.clear();
        Log.d(TAG, "ArrayList is cleared");
    }
}
