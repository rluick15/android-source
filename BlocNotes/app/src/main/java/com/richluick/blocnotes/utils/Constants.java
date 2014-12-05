package com.richluick.blocnotes.utils;

/**
 * This class containts all of the constants used in the application
 */
public class Constants {

    //Notes Constants
    public static final String SAVE_TEXT = "text";

    //Shared Preference Constants
    public static final String PREF_TYPEFACE = "font_preference";
    public static final String PREF_FONT_SIZE = "font_size_preference";

    //Database Constants
        //Database
        public static final String DATABASE_NAME = "BlocNotes";
        public static final int DATABASE_VERSION = 6;
        //Tables
        public static final String TABLE_NOTES_NAME = "Notes";
        public static final String TABLE_NOTEBOOKS_NAME = "Notebooks";
            //Initial Notebook Table
            public static final String TABLE_NOTEBOOKS_UNCATEGORIZED = "Uncategorized";
            //Table Columns
            public static final String TABLE_COLUMN_ID = "_id";
                //Notebook
                public static final String TABLE_COLUMN_NOTEBOOK_NAME = "name";
                //Notes
                public static final String TABLE_COLUMN_NOTES_BODY = "body";
                public static final String TABLE_COLUMN_NOTES_NOTEBOOK= "notebook";
                public static final String TABLE_COLUMN_NOTES_IMAGE_URL= "image_url";
                public static final String TABLE_COLUMN_NOTES_IMAGE_NAME= "image_name";

    //Intent Constants
    public static final int KEY_INTENT_REQUEST_CODE_ZERO = 0;
    public static final String KEY_NOTE_BODY = "noteBody";
    public static final String KEY_NOTE_ID = "noteID";
    public static final String KEY_NOTEBOOK_NUMBER = "notebookNumber";
    public static final String ACTION_BLANK = "";
    public static final String ACTION_SHOW_NOTIFICATION = "showNotification";
    public static final String ACTION_DISMISS = "dismiss";
    public static final String ACTION_SNOOZE = "snooze";
    public static final String ACTION_CONTENT = "content";

}
