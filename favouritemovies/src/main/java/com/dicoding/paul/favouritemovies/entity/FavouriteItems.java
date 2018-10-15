package com.dicoding.paul.favouritemovies.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.dicoding.paul.favouritemovies.database.FavouriteContract;

import static com.dicoding.paul.favouritemovies.database.FavouriteContract.getColumnInt;
import static com.dicoding.paul.favouritemovies.database.FavouriteContract.getColumnString;

public class FavouriteItems implements Parcelable {
    private int id;
    private String poster;
    private String originalTitle;
    private String releaseDate;
    private String score;
    private String overview;

    public FavouriteItems() {
    }

    public FavouriteItems (Cursor cursor) {
        this.id = getColumnInt(cursor, FavouriteContract.Notecolumns._ID);
        this.poster = getColumnString(cursor, FavouriteContract.Notecolumns.POSTER);
        this.originalTitle = getColumnString(cursor, FavouriteContract.Notecolumns.ORIGINAL_TITLE);
        this.releaseDate = getColumnString(cursor, FavouriteContract.Notecolumns.RELEASE_DATE);
        this.overview = getColumnString(cursor, FavouriteContract.Notecolumns.OVERVIEW);
        this.score = getColumnString(cursor, FavouriteContract.Notecolumns.SCORE);
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.poster);
        dest.writeString(this.originalTitle);
        dest.writeString(this.releaseDate);
        dest.writeString(this.score);
        dest.writeString(this.overview);
    }

    protected FavouriteItems(Parcel in) {
        this.id = in.readInt();
        this.poster = in.readString();
        this.originalTitle = in.readString();
        this.releaseDate = in.readString();
        this.score = in.readString();
        this.overview = in.readString();
    }

    public static final Parcelable.Creator<FavouriteItems> CREATOR = new Parcelable.Creator<FavouriteItems>() {
        @Override
        public FavouriteItems createFromParcel(Parcel source) {
            return new FavouriteItems(source);
        }

        @Override
        public FavouriteItems[] newArray(int size) {
            return new FavouriteItems[size];
        }
    };
}
