package com.richluick.blocnotes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.richluick.blocnotes.database.tables.NotebooksTable;
import com.richluick.blocnotes.database.tables.NotesTable;
import com.richluick.blocnotes.database.tables.Table;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BlocNotesDbHelper extends SQLiteOpenHelper {

    //private static BlocNotesDbHelper mInstance = null;

    // Version
    private static final int DATABASE_VERSION = 1;
    // Name
    private static final String DATABASE_NAME = "BlocNotes";
    //Table Names
    private static final String TABLE_NOTES_NAME = "Notes";
    private static final String TABLE_NOTEBOOKS_NAME = "Notebooks";
    //Tables
    private static Set<Table> sTables = new HashSet<Table>();
    static {
        sTables.add(new NotesTable());
        sTables.add(new NotebooksTable());
    }

//    public static BlocNotesDbHelper getInstance(Context context) {
//        if(mInstance == null) {
//            mInstance = new BlocNotesDbHelper(context.getApplicationContext());
//        }
//        return mInstance;
//    }

    public BlocNotesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Iterator<Table> tables = sTables.iterator();
        while (tables.hasNext()){
            sqLiteDatabase.execSQL(tables.next().getCreateStatement());
        }

        // Insert Uncategorized
        ContentValues values = new ContentValues();
        values.put("name", "Uncategorized");
        long uncategorizedRowId = sqLiteDatabase.insert(TABLE_NOTEBOOKS_NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Iterator<Table> tables = sTables.iterator();
        while (tables.hasNext()) {
            tables.next().onUpgrade(sqLiteDatabase, oldVersion, newVersion);
        }
    }
}
