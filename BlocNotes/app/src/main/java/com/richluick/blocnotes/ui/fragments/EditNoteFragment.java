package com.richluick.blocnotes.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.richluick.blocnotes.R;
import com.richluick.blocnotes.ui.activities.BlocNotes;
import com.richluick.blocnotes.utils.Constants;

public class EditNoteFragment extends DialogFragment {

    private String mNoteText;
    private EditText mUpdateNote;
    private Button mUpdateNoteButton;
    private Button mCancelButton;
    private String mNoteId;

    public EditNoteFragment() {}

    public EditNoteFragment(String noteText, String noteId) {
        this.mNoteText = noteText;
        this.mNoteId = noteId;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(Constants.SAVE_TEXT, mUpdateNote.getText().toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_note, container, false);

        //restore state of app when activity is destroyed and restarted
        mUpdateNote = (EditText) rootView.findViewById(R.id.editNoteText);
        if (savedInstanceState != null) {
            setUpdateNoteText(savedInstanceState.getString(Constants.SAVE_TEXT));
        }

        setUpdateNoteText(mNoteText);
        getDialog().setTitle(getString(R.string.title_update_note)); //set the dialog title

        //set cancel Button
        mCancelButton = (Button) rootView.findViewById(R.id.editNoteCancel);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        //set update button
        mUpdateNoteButton = (Button) rootView.findViewById(R.id.editNoteButton);
        mUpdateNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedNote = mUpdateNote.getText().toString();
                ((BlocNotes) getActivity()).updateDatabaseNewText(updatedNote, mNoteId);
                dismiss();
            }
        });

        return rootView;
    }

    /**
     * This is a setter method for setting the text to restore from savedInstanceState, to set it
     * to blank if the user clicks the erase option, or to generally change the text from outside
     * the fragment
     *
     * param text the String to insert into the EditText on the Note Fragment
     * */
    public void setUpdateNoteText(String text) {
        mUpdateNote.setText(text);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



}
