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

/**
 *
 */
public class AddNotebookFragment extends DialogFragment {

    private EditText mNotebookTitle;
    private Button mCreate;
    private Button mCancel;
    private String mNewTitle;

    private OnFragmentInteractionListener mListener;

    public AddNotebookFragment() {} // Required empty public constructor

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_notebook, container, false);
        getDialog().setTitle(getString(R.string.create_notebook)); //set the dialog title

        //Declare Resources
        mNotebookTitle = (EditText) view.findViewById(R.id.editNoteText);
        mCreate = (Button) view.findViewById(R.id.createButton);
        mCancel = (Button) view.findViewById(R.id.cancelButton);

        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNewTitle = mNotebookTitle.getText().toString();
                ((BlocNotes) getActivity()).addNotebook(mNewTitle);
                dismiss();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
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
        public void addNotebook(String newTitle);
    }

}
