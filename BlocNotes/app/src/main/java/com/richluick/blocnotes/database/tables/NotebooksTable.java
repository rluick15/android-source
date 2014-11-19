package com.richluick.blocnotes.database.tables;

import android.database.sqlite.SQLiteDatabase;

public class NotebooksTable extends Table {

    private static final String TABLE_NAME = "Notebooks";
    private static final String SQL_CREATE_NOTEBOOKS =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    "_id INTEGER PRIMARY KEY," +
                    "name TEXT" +
                    " )";

    public NotebooksTable() {
        super(TABLE_NAME);
    }

    @Override
    public String getCreateStatement() {
        return SQL_CREATE_NOTEBOOKS;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }
}
