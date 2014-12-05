package com.richluick.blocnotes.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.richluick.blocnotes.R;
import com.richluick.blocnotes.ui.activities.BlocNotes;
import com.richluick.blocnotes.utils.Constants;

public class ImageUrlFragment extends DialogFragment {

    private String mNoteId;
    private EditText mImageUrl;

    public ImageUrlFragment(String noteId) {
        this.mNoteId = noteId;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString(Constants.SAVE_TEXT, mImageUrl.getText().toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_url, container, false);
        getDialog().setTitle(getString(R.string.title_image_url)); //set the dialog title

        //restore state of app when activity is destroyed and restarted
        mImageUrl = (EditText) view.findViewById(R.id.urlText);
        if (savedInstanceState != null) {
            mImageUrl.setText(savedInstanceState.getString(Constants.SAVE_TEXT));
        }

        Button cancelButton = (Button) view.findViewById(R.id.urlCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        Button setUrlButton = (Button) view.findViewById(R.id.setUrlButton);
        setUrlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String urlText = mImageUrl.getText().toString();
                if(urlText.equals("")) {
                    Toast.makeText(getActivity(), getString(R.string.toast_valid_url), Toast.LENGTH_LONG).show();
                }
                else {
                    ((BlocNotes) getActivity()).saveImageUrl(urlText, mNoteId);
                    dismiss();
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
    public interface OnSetUrlInteractionListener {
        public void saveImageUrl(String urlText, String noteId);
    }

}
