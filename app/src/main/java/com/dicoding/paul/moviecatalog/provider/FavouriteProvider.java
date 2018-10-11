package com.dicoding.paul.moviecatalog.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.dicoding.paul.moviecatalog.database.FavouriteContract;
import com.dicoding.paul.moviecatalog.database.FavouriteHelper;

import static com.dicoding.paul.moviecatalog.database.FavouriteContract.AUTHORITY;
import static com.dicoding.paul.moviecatalog.database.FavouriteContract.CONTENT_URI;

public class FavouriteProvider extends ContentProvider {
    private static final int FAVOURITE = 1;
    private static final int FAVOURITE_ID = 2;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, FavouriteContract.TABLE_FAVOURITE, FAVOURITE);

        sUriMatcher.addURI(AUTHORITY, FavouriteContract.TABLE_FAVOURITE + "/#", FAVOURITE_ID);
    }

    private FavouriteHelper favouriteHelper;

    @Override
    public boolean onCreate() {
        favouriteHelper = new FavouriteHelper(getContext());
        favouriteHelper.open();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case FAVOURITE:
                cursor = favouriteHelper.queryProvider();
                break;
            case FAVOURITE_ID:
                cursor = favouriteHelper.queryByIdProvider(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
        }

        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long added;

        switch (sUriMatcher.match(uri)) {
            case FAVOURITE:
                added = favouriteHelper.insertProvider(values);
                break;
            default:
                added = 0;
                break;
        }

        if (added > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleted;

        switch (sUriMatcher.match(uri)) {
            case FAVOURITE_ID:
                deleted = favouriteHelper.deleteProvider(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }

        if (deleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int updated;

        switch (sUriMatcher.match(uri)) {
            case FAVOURITE_ID:
                updated = favouriteHelper.updateProvider(uri.getLastPathSegment(), values);
                break;
            default:
                updated = 0;
        }

        if (updated > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updated;
    }
}
