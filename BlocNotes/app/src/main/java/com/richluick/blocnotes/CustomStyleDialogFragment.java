package com.richluick.blocnotes;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomStyleDialogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomStyleDialogFragment#} factory method to
 * create an instance of this fragment.
 *
 */
public class CustomStyleDialogFragment extends DialogFragment {

    private Spinner mSpinnerFont;
    private String mCustomFont;
    private RadioGroup mFontSizeSelect;
    private RadioButton mSmallFont;
    private RadioButton mMediumFont;
    private RadioButton mLargeFont;
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

        //Create the font spinner and array adapter for the custom font choices
        customTypeface(view);

        //Set up the onClickListeners for choosing the font size in the following lines
        customFontStyle(view);

        return view;
    }

    /**
     * This method sets up the radio buttons for the custom font size and reacts when a
     * button is clicked.
     *
     * @param view The inflated view that the Spinner is placed in
     * @return void
     * */
    private void customFontStyle(View view) {
        mFontSizeSelect = (RadioGroup) view.findViewById(R.id.fontSize);
        mSmallFont = (RadioButton) view.findViewById(R.id.buttonSmallFont);
        mMediumFont = (RadioButton) view.findViewById(R.id.buttonMediumFont);
        mLargeFont = (RadioButton) view.findViewById(R.id.buttonLargeFont);

        mFontSizeSelect.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.buttonSmallFont) {
                    ((BlocNotes) getActivity()).onStyleChange(null, SMALL_FONT);
                }
                else if (i == R.id.buttonMediumFont) {
                    ((BlocNotes) getActivity()).onStyleChange(null, MEDIUM_FONT);
                }
                else if (i == R.id.buttonLargeFont) {
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
     * @return void
     * */
    private void customTypeface(View view) {
        mSpinnerFont = (Spinner) view.findViewById(R.id.spinnerFont);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.font_spinner_array, android.R.layout.simple_list_item_1);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerFont.setAdapter(arrayAdapter);

        mSpinnerFont.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mCustomFont = adapterView.getItemAtPosition(i).toString();
                ((BlocNotes) getActivity()).onFontChange(null, mCustomFont);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
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
