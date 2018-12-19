package com.dicoding.paul.moviecatalog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class FavouriteDatabaseHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME = "dbmoviecatalog";

    private static final int DATABASE_VERSION = 1;

    private static final String  SQL_CREATE_TABLE_NOTE = String.format("CREATE TABLE %s" +
            " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s INTEGER)",
    FavouriteContract.TABLE_FAVOURITE,
    FavouriteContract.Notecolumns._ID,
    FavouriteContract.Notecolumns.POSTER,
    FavouriteContract.Notecolumns.ORIGINAL_TITLE,
    FavouriteContract.Notecolumns.RELEASE_DATE,
    FavouriteContract.Notecolumns.OVERVIEW,
    FavouriteContract.Notecolumns.SCORE,
    FavouriteContract.Notecolumns.STATE
    );

    FavouriteDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavouriteContract.TABLE_FAVOURITE);
        onCreate(db);
    }
}
