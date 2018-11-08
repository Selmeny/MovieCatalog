package com.dicoding.paul.moviecatalog.nowplayingfragment;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

//Implements parcelable interface in this class to send its object through intent.
//Another way is by using serializable interface, it's easier but slower(?) than parcelable
public class NowPlayingItems implements Parcelable {
    private String poster;
    private String originalTitle;
    private String releaseDate;
    private String score;
    private String overview;
    private String TAG = NowPlayingItems.class.getSimpleName();
    private static final String URL_POSTER = "http://image.tmdb.org/t/p/w185";

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

    public NowPlayingItems(@NonNull JSONObject object) {

        try {
            String originalTitle = object.getString("original_title");
            String overview = object.getString("overview");

            double mScore = object.getDouble("vote_average");
            String score = String.valueOf(mScore);

            String mPoster = object.getString("poster_path");
            String poster = URL_POSTER + mPoster;

            String mDate = object.getString("release_date");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = dateFormat.parse(mDate);
            SimpleDateFormat newDateFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy", Locale.getDefault());
            String releaseDate = newDateFormat.format(date);

            this.poster = poster;
            this.originalTitle = originalTitle;
            this.releaseDate = releaseDate;
            this.score = score;
            this.overview = overview;

            Log.d(TAG, "New movie: " + originalTitle);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "object extracting failed");
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.poster);
        dest.writeString(this.originalTitle);
        dest.writeString(this.releaseDate);
        dest.writeString(this.score);
        dest.writeString(this.overview);
    }

    protected NowPlayingItems(Parcel in) {
        this.poster = in.readString();
        this.originalTitle = in.readString();
        this.releaseDate = in.readString();
        this.score = in.readString();
        this.overview = in.readString();
    }

    public static final Creator<NowPlayingItems> CREATOR = new Creator<NowPlayingItems>() {
        @Override
        public NowPlayingItems createFromParcel(Parcel source) {
            return new NowPlayingItems(source);
        }

        @Override
        public NowPlayingItems[] newArray(int size) {
            return new NowPlayingItems[size];
        }
    };
}
