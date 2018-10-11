package com.dicoding.paul.moviecatalog.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class FavouriteContract {

    public static String TABLE_FAVOURITE = "favourite";

    public static final class Notecolumns implements BaseColumns {
        public static String POSTER = "poster";
        public static String ORIGINAL_TITLE = "title";
        public static String RELEASE_DATE = "date";
        public static String OVERVIEW = "overview";
        public static String SCORE = "score";
    }

    public static final String AUTHORITY = "com.dicoding.paul.moviecatalog";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_FAVOURITE)
            .build();

    public static String getColumnString (Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt (Cursor cursor, String columnName) {
        return  cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static long getColumnLong (Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }
}
