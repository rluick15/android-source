package com.richluick.blocnotes.utils;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;

import com.richluick.blocnotes.BlocNotesApplication;
import com.richluick.blocnotes.database.tables.NotesTable;

public class PingService extends IntentService {

    private NotesTable mNotesTable = new NotesTable();

    public PingService() {
        super("PingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SQLiteDatabase db = BlocNotesApplication.get(this).getWritableDb();

        //dismiss the notification when an option is selected
        if (Context.NOTIFICATION_SERVICE != null) {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
            nMgr.cancel(Constants.KEY_INTENT_REQUEST_CODE_ZERO);
        }

        if (intent != null) {
            String noteText = intent.getStringExtra(Constants.KEY_NOTE_BODY);
            String noteId = intent.getStringExtra(Constants.KEY_NOTE_ID);

            final String action = intent.getAction();
            if ((Constants.ACTION_DISMISS).equals(action)) {
                mNotesTable.deleteNote(db, noteId);
            }
            else if ((Constants.ACTION_SNOOZE).equals(action)) {
                Intent reminderReceiverIntent = new Intent(this, ReminderReceiver.class);
                reminderReceiverIntent.setAction(Constants.ACTION_SHOW_NOTIFICATION);
                reminderReceiverIntent.putExtra(Constants.KEY_NOTE_BODY, noteText);
                reminderReceiverIntent.putExtra(Constants.KEY_NOTE_ID, noteId);
                PendingIntent reminderPendingIntent = PendingIntent.getBroadcast(this,
                        Constants.KEY_INTENT_REQUEST_CODE_ZERO,
                        reminderReceiverIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + 10 * 60 * 1000,
                        reminderPendingIntent);
            }
        }
    }
}
