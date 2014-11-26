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
        public static final int DATABASE_VERSION = 1;
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

}
