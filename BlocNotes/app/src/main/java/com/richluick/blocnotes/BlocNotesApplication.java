package com.richluick.blocnotes;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.richluick.blocnotes.database.BlocNotesDbHelper;

/**
 * Created by Rich on 11/19/2014.
 */
public class BlocNotesApplication extends Application {

    private static BlocNotesDbHelper mDatabase;

    public BlocNotesApplication() {}

    @Override
    public void onCreate() {
        mDatabase = new BlocNotesDbHelper(getApplicationContext());
    }

    public SQLiteDatabase getReadableDb() {
        return mDatabase.getReadableDatabase();
    }

    public SQLiteDatabase getWritableDb() {
        return mDatabase.getWritableDatabase();
    }

    public static BlocNotesApplication get(Context context) {
        return (BlocNotesApplication) context.getApplicationContext();
    }
}
