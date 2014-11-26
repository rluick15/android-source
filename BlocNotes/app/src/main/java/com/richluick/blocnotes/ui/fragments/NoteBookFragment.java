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
import android.widget.ListView;

import com.richluick.blocnotes.BlocNotesApplication;
import com.richluick.blocnotes.R;
import com.richluick.blocnotes.adapters.NoteAdapter;
import com.richluick.blocnotes.ui.activities.BlocNotes;
import com.richluick.blocnotes.utils.Constants;

/**
 * Created by Rich on 11/10/2014.
 */
public class NoteBookFragment extends ListFragment {

    private String mNotebookName;
    private int mNotebookNumber;

    public EditText mNewNote;
    private Typeface mHelvetica;
    private Typeface mHelveticaNeue;
    private Typeface mImpact;
    private Button mCreateNoteButton;
    private String mNewNoteText;

    private SQLiteDatabase mDb;
    //private SimpleCursorAdapter mCursorAdapter;
    private NoteAdapter mNoteAdapter;
    private Cursor mCursor;
    private ListView mNotesListView;

    public NoteBookFragment(String notebook, int notebookNumber) {
        this.mNotebookName = notebook;
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

        //Call this method in the Main Activity to ensure that mNoteBookFragment represents the
        //current fragment
        ((BlocNotes) getActivity()).setmNoteBookFragment();

        //restore state of app when activity is destroyed and restarted
        mDb = BlocNotesApplication.get(getActivity()).getReadableDb();
        mNewNote = (EditText) rootView.findViewById(R.id.newNote);
        if (savedInstanceState != null) {
            setNewNoteText(savedInstanceState.getString(Constants.SAVE_TEXT));
        }

        //setup the cursor and the note adapter objects
        mCursor = getCursor(mDb);
        mNoteAdapter = new NoteAdapter(getActivity(), mCursor, inflater);

        //Store the font assets as variables
        mHelvetica = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Helvetica_Reg.ttf");
        mHelveticaNeue = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeue_Lt.ttf");
        mImpact = Typeface.createFromAsset(getActivity().getAssets(), "fonts/impact.ttf");

        //set the onClickListener for the Create Note Button. Save the note when clicked
        mCreateNoteButton = (Button) rootView.findViewById(R.id.createNoteButton);
        mCreateNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNewNoteText = mNewNote.getText().toString();
                ContentValues values = new ContentValues();
                values.put(Constants.TABLE_COLUMN_NOTES_BODY, mNewNoteText);
                values.put(Constants.TABLE_COLUMN_NOTES_NOTEBOOK, mNotebookNumber);
                mDb.insert(Constants.TABLE_NOTES_NAME, null, values);

                //refresh the list of notes with the updated set of data
                mCursor = getCursor(mDb);
                mNoteAdapter.changeCursor(mCursor);
                mNoteAdapter.notifyDataSetChanged();

                setNewNoteText(""); //clear the text
            }
        });

        //get the singleton database from the application class
        mDb = BlocNotesApplication.get(getActivity()).getReadableDb();
        //db cursor query for Notebook names
        setListAdapter(mNoteAdapter);

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

        mDb.close(); //close the database
    }

    private Cursor getCursor(SQLiteDatabase db) {
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
