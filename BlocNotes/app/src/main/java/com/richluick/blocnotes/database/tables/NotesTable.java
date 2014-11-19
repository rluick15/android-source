package com.richluick.blocnotes.database.tables;

import android.database.sqlite.SQLiteDatabase;

public class NotesTable extends Table {

    private static final String TABLE_NAME = "Notes";
    private static final String SQL_CREATE_NOTES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    "_id INTEGER PRIMARY KEY," +
                    "body TEXT," +
                    "notebook INTEGER" +
                    " )";

    public NotesTable() {
        super(TABLE_NAME);
    }

    @Override
    public String getCreateStatement() {
        return SQL_CREATE_NOTES;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }
}
