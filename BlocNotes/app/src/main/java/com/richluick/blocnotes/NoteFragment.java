package com.richluick.blocnotes;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by Rich on 11/10/2014.
 */
public class NoteFragment extends Fragment {

    protected EditText mEditText;
    protected static final String TEXT = "text";


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(TEXT, mEditText.getText().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_note, container, false);

        mEditText = (EditText) rootView.findViewById(R.id.editText);
        if (savedInstanceState != null) {
            mEditText.setText(savedInstanceState.getString(TEXT));
        }

        return rootView;
    }
}
