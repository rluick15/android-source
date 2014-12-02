package com.richluick.blocnotes.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.richluick.blocnotes.R;
import com.richluick.blocnotes.ui.activities.BlocNotes;

public final class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ((Constants.ACTION_SHOW_NOTIFICATION).equals(intent.getAction())) {
            //get Extras
            String noteText = intent.getStringExtra(Constants.KEY_NOTE_BODY);
            String noteId = intent.getStringExtra(Constants.KEY_NOTE_ID);
            int notebookNumber = intent.getIntExtra(Constants.KEY_NOTEBOOK_NUMBER, 0);

            //set dismiss intent
            Intent dismissIntent = new Intent(context, PingService.class);
            dismissIntent.setAction(Constants.ACTION_DISMISS);
            dismissIntent.putExtra(Constants.KEY_NOTE_ID, noteId);
            dismissIntent.putExtra(Constants.KEY_NOTE_BODY, noteText);
            PendingIntent piDismiss = PendingIntent.getService(context,
                    Constants.KEY_INTENT_REQUEST_CODE_ZERO,
                    dismissIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            //set snooze intent
            Intent snoozeIntent = new Intent(context, PingService.class);
            snoozeIntent.setAction(Constants.ACTION_SNOOZE);
            snoozeIntent.putExtra(Constants.KEY_NOTE_ID, noteId);
            snoozeIntent.putExtra(Constants.KEY_NOTE_BODY, noteText);
            PendingIntent piSnooze = PendingIntent.getService(context,
                    Constants.KEY_INTENT_REQUEST_CODE_ZERO,
                    snoozeIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            //set Content intent
            Intent contentIntent = new Intent(context, BlocNotes.class);
            contentIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            contentIntent.setAction(Constants.ACTION_CONTENT);
            contentIntent.putExtra(Constants.KEY_NOTE_ID, noteId);
            contentIntent.putExtra(Constants.KEY_NOTE_BODY, noteText);
            contentIntent.putExtra(Constants.KEY_NOTEBOOK_NUMBER, notebookNumber);
            PendingIntent piContent = PendingIntent.getActivity(context,
                    Constants.KEY_INTENT_REQUEST_CODE_ZERO,
                    contentIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            //build alarm notification
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            builder.setContentIntent(piContent)
                .setContentTitle(context.getString(R.string.alarm_notification_title))
                .setSmallIcon(R.drawable.ic_stat_alarm)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(noteText))
                .addAction(R.drawable.ic_stat_dismiss,
                        context.getString(R.string.notification_delete_text), piDismiss)
                .addAction(R.drawable.ic_stat_snooze,
                        context.getString(R.string.notification_snooze_text), piSnooze);
            Notification alarmNotification = builder.build();
            //display notification
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(Constants.KEY_INTENT_REQUEST_CODE_ZERO, alarmNotification);
        }
    }
}
