package com.richluick.blocnotes;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by Rich on 11/10/2014.
 */
public class NoteFragment extends Fragment {

    public EditText mEditText;
    private Typeface mHelvetica;
    private Typeface mHelveticaNeue;
    private Typeface mImpact;

    private static final String TEXT = "text";

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(TEXT, mEditText.getText().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_note, container, false);
        setHasOptionsMenu(true);

        //restore state of app when activity is destroyed and restarted
        mEditText = (EditText) rootView.findViewById(R.id.editText);
        if (savedInstanceState != null) {
            setNoteText(savedInstanceState.getString(TEXT));
        }

        //Store the font assets as variables
        mHelvetica = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Helvetica_Reg.ttf");
        mHelveticaNeue = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeue_Lt.ttf");
        mImpact = Typeface.createFromAsset(getActivity().getAssets(), "fonts/impact.ttf");

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        //set the new shared preferences when returning from the Settings Fragment
        ((BlocNotes) getActivity()).setSharedPrefs();
    }

    /**
     * This is a setter method for setting the text to restore from savedInstanceState, to set it
     * to blank if the user clicks the erase option, or to generally change the text from outside
     * the fragment
     *
     * param text the String to insert into the EditText on the Note Fragment
     * */
    public void setNoteText(String text) {
        mEditText.setText(text);
    }

    /**
     * This is a setter method for setting the font the user has selected from the spinner
     *
     * param fontName the name of the font the user selected
     * @return void
     * */
    public void setCustomFont(String fontName) {
        if(mEditText != null) {
            if (fontName.equals("Helvetica")) {
                mEditText.setTypeface(mHelvetica);
            }
            else if (fontName.equals("Helvetica-Neue")) {
                mEditText.setTypeface(mHelveticaNeue);
            }
            else if (fontName.equals("Impact")) {
                mEditText.setTypeface(mImpact);
            }
            else {
                mEditText.setTypeface(Typeface.DEFAULT);
            }
        }
    }

    /**
     * This is a setter method for setting the font style the user has selected from custom menu
     *
     * param styleId the integer id of the font stlye selected (SMALL, MEDIUM, LARGE)
     * @return void
     * */
    public void setCustomStyle(int styleId) {
        if(mEditText != null) {
            if (styleId == 1) {
                mEditText.setTextAppearance(getActivity(), android.R.style.TextAppearance_Small);
            }
            else if (styleId == 2) {
                mEditText.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
            }
            else if (styleId == 3) {
                mEditText.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
            }
        }
    }

    //TODO: Only make erase and settings appear when this fragment is up
}
