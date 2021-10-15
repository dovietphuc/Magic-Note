package phucdv.android.magicnote.alarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import phucdv.android.magicnote.MagicNoteActivity;
import phucdv.android.magicnote.R;

public class AlarmReceiver extends BroadcastReceiver {
    public static String ACTION_SET_UP_ALARM = "phucdv.android.action.SET_UP_ALARM";
    public static String ACTION_FIRE_ALARM = "phucdv.android.action.FIRE_ALARM";

    private NotificationManager mNotificationManager;
    // Notification ID.
    private static final int NOTIFICATION_ID = 0;
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(ACTION_SET_UP_ALARM.equals(action)){

        } else if(ACTION_FIRE_ALARM.equals(action)){

        }
    }

    /**
     * Builds and delivers the notification.
     *
     * @param context, activity context.
     */
    private void deliverNotification(Context context, String title, String content) {
        // Create the content intent for the notification, which launches
        // this activity
        Intent contentIntent = new Intent(context, MagicNoteActivity.class);

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (context, NOTIFICATION_ID, contentIntent, PendingIntent
                        .FLAG_UPDATE_CURRENT);
        // Build the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder
                (context, PRIMARY_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_twotone_access_alarm_24)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(contentPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL);

        // Deliver the notification
        mNotificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
