package com.richluick.blocnotes;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by Rich on 11/10/2014.
 */
public class NoteFragment extends Fragment {

    public EditText mEditText;
    protected static final String TEXT = "text";
    private Typeface mHelvetica;
    private Typeface mHelveticaNeue;
    private Typeface mImpact;

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(TEXT, mEditText.getText().toString());
    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState){
//        super.onRestoreInstanceState(savedInstanceState);
//
//        mEditText = (EditText) getActivity().findViewById(R.id.editText);
//        if (savedInstanceState != null) {
//            mEditText.setText(savedInstanceState.getString(TEXT));
//        }
//    }

    //Try onRestoreInstanceState

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_note, container, false);
        setHasOptionsMenu(true);

        //restore state of app when activity is destroyed and restarted
        mEditText = (EditText) rootView.findViewById(R.id.editText);
        if (savedInstanceState != null) {
            mEditText.setText(savedInstanceState.getString(TEXT));
        }

        //Store the font assets as variables
        mHelvetica = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Helvetica_Reg.ttf");
        mHelveticaNeue = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeue_Lt.ttf");
        mImpact = Typeface.createFromAsset(getActivity().getAssets(), "fonts/impact.ttf");

        return rootView;
    }

    /**
     * This is a setter method for setting the font the user has selected from the spinner
     *
     * param fontName the name of the font the user selected
     * @return void
     * */
    public void setCustomFont(String fontName) {
        if(fontName.equals("Helvetica")) {
            mEditText.setTypeface(mHelvetica);
        }
        else if(fontName.equals("Helvetica-Neue")) {
            mEditText.setTypeface(mHelveticaNeue);
        }
        else if(fontName.equals("Impact")) {
            mEditText.setTypeface(mImpact);
        }
        else {
            mEditText.setTypeface(Typeface.DEFAULT);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu, inflater);

        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.bloc_notes, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_erase) {
            mEditText.setText("");
        }
        return super.onOptionsItemSelected(item);
    }
}
