package com.dicoding.paul.moviecatalog.alarmmanager;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.dicoding.paul.moviecatalog.BuildConfig;
import com.dicoding.paul.moviecatalog.MainActivity;
import com.dicoding.paul.moviecatalog.R;
import com.dicoding.paul.moviecatalog.nowplayingfragment.NowPlayingItems;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String TYPE_DAILY_CONTENTS = "daily_contents";
    public static final String TYPE_REMINDER = "now_playing_reminder";
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_MESSAGE = "message";

    private int NOTIF_ID_DAILY_CONTENTS = 100;
    private int NOTIF_ID_REMINDER = 200;

    private String TAG = getClass().getSimpleName();

    public AlarmReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        String type = intent.getStringExtra(EXTRA_TYPE);
        final int notifId = type.equalsIgnoreCase(TYPE_DAILY_CONTENTS) ? NOTIF_ID_DAILY_CONTENTS : NOTIF_ID_REMINDER;

        if (notifId == NOTIF_ID_DAILY_CONTENTS) {
            String title = intent.getStringExtra(EXTRA_TITLE);
            String message = intent.getStringExtra(EXTRA_MESSAGE);
            showAlarmNotification(context, title, message, notifId);
        } else {
            //Use goAsync to get total 10 seconds for broadcast receiver to execute its contents
            final PendingResult result = goAsync();
            Thread thread = new Thread() {
                public void run() {
                    loadNowPlaying(context);
                    result.setResultCode(notifId);
                    result.finish();
                }
            };
            thread.start();
        }
        Log.d(TAG, "type received: " + notifId);
    }

    private void loadNowPlaying(final Context context) {
        final String API_KEY = BuildConfig.API_KEY;
        final String URL_MAIN = "https://api.themoviedb.org/3/movie/now_playing?api_key=";
        final String URL_QUERY = "&language=en-US";

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy", Locale.getDefault());
        Date date = new Date();
        final String today = dateFormat.format(date);

        String url = URL_MAIN + API_KEY + URL_QUERY;

        SyncHttpClient client = new SyncHttpClient();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                setUseSynchronousMode(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i=0; i<list.length(); i++) {
                        JSONObject movie = list.getJSONObject(i);
                        NowPlayingItems nowPlayingItems = new NowPlayingItems(movie);

                        if (nowPlayingItems.getReleaseDate().equals(today)) {
                            String title = nowPlayingItems.getOriginalTitle();
                            String message = title + context.getString(R.string.is_now_playing);
                            showAlarmNotification(context, title, message, NOTIF_ID_REMINDER++);

                            Log.d(TAG, "Movie added: " + title);
                        } else {
                            Log.d(TAG, "Is not playing for today");
                        }
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
    }

    public void showAlarmNotification(Context context, String title, String message, int notifId) {
        String channelId = context.getString(R.string.channel_id);
        String channelName = context.getString(R.string.channel_name);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName,
                    NotificationManager.IMPORTANCE_HIGH));
        }

        Intent intent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(notifId, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_new_releases_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notificationManager.notify(notifId, builder.build());
    }

    public void setReminders(Context context, String type) {
        if (type.equalsIgnoreCase(TYPE_DAILY_CONTENTS)) {
            String title = context.getString(R.string.app_name);
            String message = context.getString(R.string.new_contents);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(EXTRA_TYPE, type);
            intent.putExtra(EXTRA_TITLE, title);
            intent.putExtra(EXTRA_MESSAGE, message);

            String SCHEDULE_DAILY_CONTENTS = "07:00";
            String timeArray[] = SCHEDULE_DAILY_CONTENTS.split(":");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
            calendar.set(Calendar.SECOND, 0);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID_DAILY_CONTENTS, intent, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);

            Toast.makeText(context, context.getString(R.string.toast_daily_contents), Toast.LENGTH_SHORT).show();

            Log.d(TAG, "Daily contents reminder is activated");

        } else {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra(EXTRA_TYPE, type);

            String SCHEDULE_REMINDER = "08:00";
            String timeArray[] = SCHEDULE_REMINDER.split(":");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
            calendar.set(Calendar.SECOND, 0);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIF_ID_REMINDER, intent, 0);
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);

            Toast.makeText(context, context.getString(R.string.toast_reminder), Toast.LENGTH_SHORT).show();

            Log.d(TAG, "Now playing reminder is activated");
        }
    }

    public void cancelAlarm(Context context, String type)  {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_DAILY_CONTENTS) ? NOTIF_ID_DAILY_CONTENTS : NOTIF_ID_REMINDER;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);
        alarmManager.cancel(pendingIntent);

        if (requestCode == NOTIF_ID_DAILY_CONTENTS) {
            Toast.makeText(context, context.getString(R.string.toast_daily_contents_off), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Daily content reminder is deactivated");
        } else {
            Toast.makeText(context, context.getString(R.string.toast_reminder_off), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Now playing reminder is deactivated");
        }
    }
}

