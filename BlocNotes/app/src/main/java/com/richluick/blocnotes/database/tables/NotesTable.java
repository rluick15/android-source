package com.richluick.blocnotes.database.tables;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.richluick.blocnotes.utils.Constants;

public class NotesTable extends Table {

    private static final String SQL_CREATE_NOTES =
            "CREATE TABLE " + Constants.TABLE_NOTES_NAME + " (" +
                    Constants.TABLE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Constants.TABLE_COLUMN_NOTES_BODY + " TEXT," +
                    Constants.TABLE_COLUMN_NOTES_NOTEBOOK + " INTEGER," +
                    Constants.TABLE_COLUMN_NOTES_IMAGE_URL + " TEXT," +
                    Constants.TABLE_COLUMN_NOTES_IMAGE_NAME + " TEXT" +
                    " )";

    public NotesTable() {
        super(Constants.TABLE_NOTES_NAME);
    }

    @Override
    public String getCreateStatement() {
        return SQL_CREATE_NOTES;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {}

    public Cursor notesQuery(SQLiteDatabase db, int notebookNumber) {
        return db.query(Constants.TABLE_NOTES_NAME,
                new String[]{Constants.TABLE_COLUMN_ID, Constants.TABLE_COLUMN_NOTES_BODY,
                        Constants.TABLE_COLUMN_NOTES_IMAGE_URL,
                        Constants.TABLE_COLUMN_NOTES_IMAGE_NAME},
                Constants.TABLE_COLUMN_NOTES_NOTEBOOK + " IS ?",
                new String[]{String.valueOf(notebookNumber)}, null, null, null, null);
    }

    public void addNewNote(SQLiteDatabase db, String noteText, int notebookNumber) {
        ContentValues values = new ContentValues();
        values.put(Constants.TABLE_COLUMN_NOTES_BODY, noteText);
        values.put(Constants.TABLE_COLUMN_NOTES_NOTEBOOK, notebookNumber);
        db.insert(Constants.TABLE_NOTES_NAME, null, values);
    }

    public void editNote(SQLiteDatabase db, String updatedNote, String noteId) {
        ContentValues values = new ContentValues();
        values.put(Constants.TABLE_COLUMN_NOTES_BODY, updatedNote);
        db.update(Constants.TABLE_NOTES_NAME, values,
                Constants.TABLE_COLUMN_ID + " = ?", new String[]{noteId});
    }

    public void addImageUrl(SQLiteDatabase db, String imageUrl, String noteId) {
        ContentValues values = new ContentValues();
        values.put(Constants.TABLE_COLUMN_NOTES_IMAGE_URL, imageUrl);
        db.update(Constants.TABLE_NOTES_NAME, values,
                Constants.TABLE_COLUMN_ID + " = ?", new String[]{noteId});
        //Log.e("ERROR", imageUrl);
    }

    public void addImageName(SQLiteDatabase db, String imageName, String noteId) {
        ContentValues values = new ContentValues();
        values.put(Constants.TABLE_COLUMN_NOTES_IMAGE_NAME, imageName);
        db.update(Constants.TABLE_NOTES_NAME, values,
                Constants.TABLE_COLUMN_ID + " = ?", new String[]{noteId});
    }

    public void deleteNote(SQLiteDatabase db, String noteId) {
        db.delete(Constants.TABLE_NOTES_NAME,
                Constants.TABLE_COLUMN_ID + " = ?", new String[]{noteId});
    }
}
