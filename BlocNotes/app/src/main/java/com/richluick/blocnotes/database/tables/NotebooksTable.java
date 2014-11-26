package com.richluick.blocnotes.database.tables;

import android.database.sqlite.SQLiteDatabase;

import com.richluick.blocnotes.utils.Constants;

public class NotebooksTable extends Table {

    private static final String SQL_CREATE_NOTEBOOKS =
            "CREATE TABLE " + Constants.TABLE_NOTEBOOKS_NAME + " (" +
                    Constants.TABLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Constants.TABLE_COLUMN_NOTEBOOK_NAME + " TEXT" +
                    " )";

    public NotebooksTable() {
        super(Constants.TABLE_NOTEBOOKS_NAME);
    }

    @Override
    public String getCreateStatement() {
        return SQL_CREATE_NOTEBOOKS;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }
}
