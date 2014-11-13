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
    private OnFragmentInteractionListener mListener;

    public CustomStyleDialogFragment() {} //required empty constructor

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_style_dialog, container, false);
        getDialog().setTitle(getString(R.string.customize_style_dialog_title)); //set the dialog title

        //Create the font spinner and array adapter for the custom font choices
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

        return view;
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
