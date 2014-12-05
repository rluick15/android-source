package com.richluick.blocnotes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.richluick.blocnotes.database.tables.NotebooksTable;
import com.richluick.blocnotes.database.tables.NotesTable;
import com.richluick.blocnotes.database.tables.Table;
import com.richluick.blocnotes.utils.Constants;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BlocNotesDbHelper extends SQLiteOpenHelper {

    //Create Tables
    private static Set<Table> sTables = new HashSet<Table>();
    static {
        sTables.add(new NotesTable());
        sTables.add(new NotebooksTable());
    }

    public BlocNotesDbHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Iterator<Table> tables = sTables.iterator();
        while (tables.hasNext()){
            sqLiteDatabase.execSQL(tables.next().getCreateStatement());
        }

        // Insert Uncategorized
        ContentValues values = new ContentValues();
        values.put(Constants.TABLE_COLUMN_NOTEBOOK_NAME, Constants.TABLE_NOTEBOOKS_UNCATEGORIZED);
        sqLiteDatabase.insert(Constants.TABLE_NOTEBOOKS_NAME, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (oldVersion == 5 && newVersion == 6) {
            sqLiteDatabase.execSQL("ALTER TABLE " + Constants.TABLE_NOTES_NAME +
                    " ADD COLUMN " + Constants.TABLE_COLUMN_NOTES_IMAGE_NAME + " TEXT");
        }
    }
}
