package com.richluick.blocnotes.ui.fragments;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.richluick.blocnotes.R;
import com.richluick.blocnotes.ui.activities.BlocNotes;
import com.richluick.blocnotes.utils.SharedPreferanceConstants;


/**
 * This fragment display the application options in a dialog
 */
public class CustomStyleDialogFragment extends DialogFragment {

    private String mCustomFont;
    private OnFragmentInteractionListener mListener;

    //setup constant integer values to represent font sizes for when the user picks a custom font
    private static final int SMALL_FONT = 1;
    private static final int MEDIUM_FONT = 2;
    private static final int LARGE_FONT = 3;

    public CustomStyleDialogFragment() {} //required empty constructor

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_style_dialog, container, false);
        getDialog().setTitle(getString(R.string.customize_style_dialog_title)); //set the dialog title

        //setup shared preferences for Custom Settings
        SharedPreferences sharedPrefs = getActivity().getPreferences(0);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        //Create the font spinner and array adapter for the custom font choices
        customTypeface(view, editor);

        //Set up the onClickListeners for choosing the font size in the following lines
        customFontStyle(view, editor);

        return view;
    }

    /**
     * This method sets up the radio buttons for the custom font size and reacts when a
     * button is clicked.
     *
     * @param view The inflated view that the Spinner is placed in
     * @param editor To store the value in SharedPreferences
     * */
    private void customFontStyle(View view, final SharedPreferences.Editor editor) {
        RadioGroup fontSizeSelect = (RadioGroup) view.findViewById(R.id.fontSize);
        fontSizeSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.buttonSmallFont) {
                    editor.putInt(SharedPreferanceConstants.PREF_FONT_SIZE, SMALL_FONT);
                    editor.apply();
                    ((BlocNotes) getActivity()).onStyleChange(null, SMALL_FONT);
                }
                else if (i == R.id.buttonMediumFont) {
                    editor.putInt(SharedPreferanceConstants.PREF_FONT_SIZE, MEDIUM_FONT);
                    editor.apply();
                    ((BlocNotes) getActivity()).onStyleChange(null, MEDIUM_FONT);
                }
                else if (i == R.id.buttonLargeFont) {
                    editor.putInt(SharedPreferanceConstants.PREF_FONT_SIZE, LARGE_FONT);
                    editor.apply();
                    ((BlocNotes) getActivity()).onStyleChange(null, LARGE_FONT);
                }
            }
        });
    }

    /**
     * This method sets up the spinner to hold a list of custom fonts the user can pick from and
     * reacts when a spinner item is clicks
     *
     * @param view The inflated view that the Spinner is placed in
     * @param editor To store the value in SharedPreferences
     * */
    private void customTypeface(View view, final SharedPreferences.Editor editor) {
        Spinner spinnerFont = (Spinner) view.findViewById(R.id.spinnerFont);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.font_spinner_array, android.R.layout.simple_list_item_1);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFont.setAdapter(arrayAdapter);

        spinnerFont.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCustomFont = adapterView.getItemAtPosition(i).toString();
                editor.putString(SharedPreferanceConstants.PREF_TYPEFACE, mCustomFont);
                editor.apply();
                ((BlocNotes) getActivity()).onFontChange(null, mCustomFont);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onStyleChange(CustomStyleDialogFragment dialog, int styleId);
        public void onFontChange(CustomStyleDialogFragment dialog, String fontName);
        public void onThemeChange(CustomStyleDialogFragment dialog, int themeId);
    }

}
