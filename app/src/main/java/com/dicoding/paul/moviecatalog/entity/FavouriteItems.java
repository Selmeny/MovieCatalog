package com.dicoding.paul.moviecatalog.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static android.provider.BaseColumns._ID;
import static com.dicoding.paul.moviecatalog.database.FavouriteContract.Notecolumns.ORIGINAL_TITLE;
import static com.dicoding.paul.moviecatalog.database.FavouriteContract.Notecolumns.OVERVIEW;
import static com.dicoding.paul.moviecatalog.database.FavouriteContract.Notecolumns.POSTER;
import static com.dicoding.paul.moviecatalog.database.FavouriteContract.Notecolumns.RELEASE_DATE;
import static com.dicoding.paul.moviecatalog.database.FavouriteContract.Notecolumns.SCORE;
import static com.dicoding.paul.moviecatalog.database.FavouriteContract.Notecolumns.STATE;
import static com.dicoding.paul.moviecatalog.database.FavouriteContract.getColumnInt;
import static com.dicoding.paul.moviecatalog.database.FavouriteContract.getColumnString;

public class FavouriteItems implements Parcelable {
    private int id;
    private String poster;
    private String originalTitle;
    private String releaseDate;
    private String score;
    private String overview;
    private int state;

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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public FavouriteItems(Cursor cursor) {
        this.id = getColumnInt(cursor, _ID);
        this.poster = getColumnString(cursor, POSTER);
        this.originalTitle = getColumnString(cursor, ORIGINAL_TITLE);
        this.releaseDate = getColumnString(cursor, RELEASE_DATE);
        this.overview = getColumnString(cursor, OVERVIEW);
        this.score = getColumnString(cursor, SCORE);
        this.state = getColumnInt(cursor, STATE);
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
        dest.writeInt(this.state);
    }

    protected FavouriteItems(Parcel in) {
        this.id = in.readInt();
        this.poster = in.readString();
        this.originalTitle = in.readString();
        this.releaseDate = in.readString();
        this.score = in.readString();
        this.overview = in.readString();
        this.state = in.readInt();
    }

    public static final Creator<FavouriteItems> CREATOR = new Creator<FavouriteItems>() {
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
