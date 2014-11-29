package com.richluick.blocnotes.ui.fragments;

import android.app.ListFragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.richluick.blocnotes.R;
import com.richluick.blocnotes.adapters.NoteAdapter;
import com.richluick.blocnotes.ui.activities.BlocNotes;
import com.richluick.blocnotes.utils.Constants;

public class NoteBookFragment extends ListFragment {

    private int mNotebookNumber;

    public EditText mNewNote;
    private static Typeface mHelvetica;
    private static Typeface mHelveticaNeue;
    private static Typeface mImpact;
    private String mNewNoteText;

    //private SimpleCursorAdapter mCursorAdapter;
    private NoteAdapter mNoteAdapter;
    private LayoutInflater mInflater;

    public NoteBookFragment(int notebookNumber) {
        this.mNotebookNumber = notebookNumber;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(Constants.SAVE_TEXT, mNewNote.getText().toString());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notebook, container, false);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        mInflater = inflater;

        //Call this method in the Main Activity to ensure that mNoteBookFragment represents the
        //current fragment
        ((BlocNotes) getActivity()).setNoteBookFragment();

        //restore state of app when activity is destroyed and restarted
        mNewNote = (EditText) rootView.findViewById(R.id.newNote);
        if (savedInstanceState != null) {
            setNewNoteText(savedInstanceState.getString(Constants.SAVE_TEXT));
        }

        //Store the font assets as variables
        mHelvetica = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Helvetica_Reg.ttf");
        mHelveticaNeue = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeue_Lt.ttf");
        mImpact = Typeface.createFromAsset(getActivity().getAssets(), "fonts/impact.ttf");

        //Call the method in the main activity to get the cursor and set the adapter
        ((BlocNotes) getActivity()).getNotebookCursorInBackground();

        //set the onClickListener for the Create Note Button. Save the note when clicked
        Button createNoteButton = (Button) rootView.findViewById(R.id.createNoteButton);
        createNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNewNoteText = mNewNote.getText().toString();
                ContentValues values = new ContentValues();
                values.put(Constants.TABLE_COLUMN_NOTES_BODY, mNewNoteText);
                values.put(Constants.TABLE_COLUMN_NOTES_NOTEBOOK, mNotebookNumber);
                ((BlocNotes) getActivity()).createNewNote(values);
            }
        });

        //Simple cursor adapter for displaying notebook names in custom listview
//        mCursorAdapter = new SimpleCursorAdapter(getActivity(),
//                R.layout.note_adapter,
//                cursor,
//                new String[] {Constants.TABLE_COLUMN_NOTES_BODY},
//                new int[] {R.id.noteText});
//
//        mNotesListView = (ListView) rootView.findViewById(android.R.id.list);
//        mNotesListView.setAdapter(mCursorAdapter);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        //set the new shared preferences when returning from the Settings Fragment
        ((BlocNotes) getActivity()).setSharedPrefs();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * This method is called on the Notebook object from the main activity. It takes a cursor object
     * and refreshes the note list when a new note is added. It also clears the text field
     *
     * param cursor The cursor object with the new database query stored in it
     * */
    public void refreshNoteList(Cursor cursor) {
        mNoteAdapter.changeCursor(cursor);
        mNoteAdapter.notifyDataSetChanged();

        setNewNoteText(""); //clear the text

    }

    /**
     * This method is called on the Notebook object from the main activity. It takes a cursor object
     * and sets the note adapter using it
     *
     * param cursor The cursor object with the database query stored in it
     * */
    public void setNotebookAdapter(Cursor cursor) {
        mNoteAdapter = new NoteAdapter(getActivity(), cursor, mInflater);
        setListAdapter(mNoteAdapter);
    }

    /**
     * This method takes a database object and gets the cursor specific to the database of
     * notes being displayed. It then returns the cursor object
     *
     * param db the database being queried
     * return Cursor the cursor object with the queried data
     * */
    public Cursor getCursor(SQLiteDatabase db) {
        return db.query(Constants.TABLE_NOTES_NAME,
                new String[] {Constants.TABLE_COLUMN_ID, Constants.TABLE_COLUMN_NOTES_BODY},
                Constants.TABLE_COLUMN_NOTES_NOTEBOOK + " IS ?",
                new String[]{String.valueOf(mNotebookNumber)}, null, null, null, null);
    }

    /**
     * This is a setter method for setting the text to restore from savedInstanceState, to set it
     * to blank if the user clicks the erase option, or to generally change the text from outside
     * the fragment
     *
     * param text the String to insert into the EditText on the Note Fragment
     * */
    public void setNewNoteText(String text) {
        mNewNote.setText(text);
    }

    /**
     * This is a setter method for setting the font the user has selected from the spinner.
     * Also calls the method to set the font size in the list adapter
     *
     * param fontName the name of the font the user selected
     * */
    public void setCustomFont(String fontName) {
        if(mNewNote != null) {
            if (fontName.equals("Helvetica")) {
                mNewNote.setTypeface(mHelvetica);
               // mNoteAdapter.setCustomFont(mHelvetica);
            }
            else if (fontName.equals("Helvetica-Neue")) {
                mNewNote.setTypeface(mHelveticaNeue);
               // mNoteAdapter.setCustomFont(mHelveticaNeue);
            }
            else if (fontName.equals("Impact")) {
                mNewNote.setTypeface(mImpact);
                //mNoteAdapter.setCustomFont(mImpact);
            }
            else {
                mNewNote.setTypeface(Typeface.DEFAULT);
                //mNoteAdapter.setCustomFont(Typeface.DEFAULT);
            }
        }
    }

    /**
     * This is a setter method for setting the font style the user has selected from custom menu
     *
     * param styleId the integer id of the font stlye selected (SMALL, MEDIUM, LARGE)
     * */
    public void setCustomStyle(int styleId) {
        if(mNewNote != null) {
            if (styleId == 1) {
                mNewNote.setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
            }
            else if (styleId == 2) {
                mNewNote.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
            }
            else if (styleId == 3) {
                mNewNote.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
            }

            //mNoteAdapter.setCustomStyle(styleId); //set the style for the list adapter
        }
    }

    //TODO: Only make erase and settings appear when this fragment is up
}