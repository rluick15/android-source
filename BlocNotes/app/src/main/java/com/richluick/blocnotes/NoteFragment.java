package com.richluick.blocnotes;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

/**
 * Created by Rich on 11/10/2014.
 */
public class NoteFragment extends Fragment {
    public NoteFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        RelativeLayout relativeLayout = new RelativeLayout(getActivity());

        EditText editText = new EditText(getActivity());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        relativeLayout.addView(editText, params);

        return relativeLayout;
    }
}
