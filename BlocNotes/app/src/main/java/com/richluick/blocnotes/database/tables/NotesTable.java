package com.richluick.blocnotes.database.tables;

import android.database.sqlite.SQLiteDatabase;

import com.richluick.blocnotes.utils.Constants;

public class NotesTable extends Table {

    private static final String SQL_CREATE_NOTES =
            "CREATE TABLE " + Constants.TABLE_NOTES_NAME + " (" +
                    Constants.TABLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Constants.TABLE_COLUMN_NOTES_BODY + " TEXT," +
                    Constants.TABLE_COLUMN_NOTES_NOTEBOOK + " INTEGER" +
                    " )";

    public NotesTable() {
        super(Constants.TABLE_NOTES_NAME);
    }

    @Override
    public String getCreateStatement() {
        return SQL_CREATE_NOTES;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }
}
