package com.dicoding.paul.moviecatalog.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.dicoding.paul.moviecatalog.R;
import com.dicoding.paul.moviecatalog.entity.FavouriteItems;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.dicoding.paul.moviecatalog.database.FavouriteContract.CONTENT_URI;

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private int mAppWidgetId;
    private String TAG = StackRemoteViewsFactory.class.getSimpleName();
    private List<FavouriteItems> favItemsList = new ArrayList<>();

    StackRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        loadData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return favItemsList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Bitmap bitmap = null;

        FavouriteItems favItems = favItemsList.get(position);
        String poster = favItems.getPoster();
        String title = favItems.getOriginalTitle();

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.items_widget);

        try {
            bitmap = Glide.with(mContext)
                    .asBitmap()
                    .load(poster)
                    .submit()
                    .get();

            Log.d(TAG, "Picture Loaded: " + title);

        } catch (InterruptedException|ExecutionException e) {
            Log.e(TAG, "Glide Error");
        }

        Bundle extras = new Bundle();
        extras.putString(ImageBannerWidget.EXTRA_ITEM, title);

        Intent fillIntent = new Intent();
        fillIntent.putExtras(extras);

        remoteViews.setImageViewBitmap(R.id.img_widget, bitmap);
        remoteViews.setOnClickFillInIntent(R.id.img_widget, fillIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    //Use this method to load data from database
    void loadData() {
        final long identityToken = Binder.clearCallingIdentity();
        favItemsList.clear();
        Cursor cursor = mContext.getContentResolver().query(CONTENT_URI, null, null,
                null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                FavouriteItems favouriteItems = new FavouriteItems(cursor);
                favItemsList.add(favouriteItems);

                Log.d(TAG, "Movie Loaded: " + favouriteItems.getOriginalTitle());
            } while (cursor.moveToNext());
            cursor.close();
        }
        Binder.restoreCallingIdentity(identityToken);
    }
}
