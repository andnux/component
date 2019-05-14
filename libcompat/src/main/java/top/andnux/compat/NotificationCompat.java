package top.andnux.compat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;

import java.lang.ref.WeakReference;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationCompat {

    private final String CHANNEL_ID;
    private final String GROUP_ID;

    private WeakReference<Context> mContextReference;
    private NotificationManager mNotificationManager;
    private NotificationChannel mNotificationChannel;
    private androidx.core.app.NotificationCompat.Builder mNotification;

    public static final int DEFAULT_ALL = ~0;
    public static final int DEFAULT_SOUND = 1;
    public static final int DEFAULT_VIBRATE = 2;
    public static final int DEFAULT_LIGHTS = 4;


    public static final int PRIORITY_DEFAULT = 0;
    public static final int PRIORITY_LOW = -1;
    public static final int PRIORITY_MIN = -2;
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MAX = 2;

    public void deleteNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.deleteNotificationChannel(CHANNEL_ID);
        }
        mNotificationManager.notify();
    }

    public void notify(String tag, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && mNotificationChannel == null) {
            mNotificationChannel = makeDefaultNotificationChannel();
        }
        if (mNotification != null) {
            mNotificationManager.notify(tag, id, mNotification.build());
        }
    }

    public void notify(int id, Notification notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && mNotificationChannel == null) {
            mNotificationChannel = makeDefaultNotificationChannel();
        }
        if (mNotification != null) {
            mNotificationManager.notify(id, mNotification.build());
        }
    }

    public void cancel(String tag, int id) {
        mNotificationManager.cancel(tag, id);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private NotificationChannel makeDefaultNotificationChannel() {
        Context context = mContextReference.get();
        String appName = SystemSupport.getAppName(context);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, appName,
                NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableVibration(true);
        channel.enableLights(true);
        mNotificationManager.createNotificationChannel(channel);
        return channel;
    }


    public void cancel(int id) {
        mNotificationManager.cancel(id);
    }

    public void cancelAll() {
        mNotificationManager.cancelAll();
    }

    public void deleteNotificationChannelGroup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationManager.deleteNotificationChannelGroup(GROUP_ID);
        }
    }

    private NotificationCompat(Context context) {
        mContextReference = new WeakReference<>(context);
        mNotificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        CHANNEL_ID = context.getPackageName() + ".CHANNEL_ID";
        GROUP_ID = context.getPackageName() + ".GROUP_ID";
    }

    public static NotificationCompat with(Context context) {
        return new NotificationCompat(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationCompat withChannel(NotificationChannel channel) {
        mNotificationManager.createNotificationChannel(channel);
        mNotificationChannel = channel;
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationCompat withChannel(String channelId,
                                          String channelName,
                                          int importance, boolean isVibrate,
                                          boolean hasSound) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        Context context = mContextReference.get();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                NOTIFICATION_SERVICE);
        channel.enableVibration(isVibrate);
        channel.enableLights(true);
        if (!hasSound) {
            channel.setSound(null, null);
        }
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
        mNotificationChannel = channel;
        return this;
    }

    public NotificationCompat withNotification(androidx.core.app.NotificationCompat.Builder
                                                       notification) {
        mNotification = notification;
        return this;
    }


    public NotificationCompat withNotificationMessage(String title, String text,
                                                      PendingIntent intent) {
        Context context = mContextReference.get();
        mNotification = new
                androidx.core.app.NotificationCompat.Builder(context, CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentTitle(title)
                .setContentText(text)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true)
                .setNumber(15)
                .setContentIntent(intent);
        return this;
    }

    public NotificationCompat withNotificationProgress(int icon, String title, String textPrefix, int max, int progress,
                                                       boolean indeterminate,
                                                       PendingIntent intent) {
        if (mNotification == null) {
            Context context = mContextReference.get();
            mNotification = new
                    androidx.core.app.NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(icon)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentTitle(title)
                    .setContentText(textPrefix + progress + "%")
                    .setProgress(max, progress, indeterminate)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setNumber(15)
                    .setContentIntent(intent);
        }
        mNotification = mNotification.setProgress(max, progress, indeterminate)
                .setContentText(textPrefix + progress + "%");
        return this;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public NotificationChannelGroup makeNotificationChannelGroup(String id, CharSequence name) {
        return new NotificationChannelGroup(id, name);
    }

    private void requestPermission(Activity activity, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        activity.startActivity(intent);
    }
}
