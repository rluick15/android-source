package com.richluick.blocnotes;

import android.app.Application;
import android.content.Context;

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

    public BlocNotesDbHelper getDatabase() {
        return mDatabase;
    }

    public static BlocNotesApplication get(Context context) {
        return (BlocNotesApplication) context.getApplicationContext();
    }
}
