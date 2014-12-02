package com.richluick.blocnotes.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.richluick.blocnotes.R;
import com.richluick.blocnotes.ui.activities.BlocNotes;

public class SetReminderFragment extends DialogFragment {

    private String mNoteText;
    private String mNoteId;
    private int mReminderKey;

    public SetReminderFragment() {}

    public SetReminderFragment(String noteText, String noteId) {
        this.mNoteText = noteText;
        this.mNoteId = noteId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_set_reminder, container, false);

        getDialog().setTitle(getString(R.string.title_set_reminder)); //set the dialog title

        //setup the spinner to get the item selected
        Spinner spinnerTime = (Spinner) rootView.findViewById(R.id.spinnerReminderTime);
        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.reminder_time_array, android.R.layout.simple_list_item_1);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(arrayAdapter);
        spinnerTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mReminderKey = i;
                Log.e("ERROR", String.valueOf(i));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        //cancel button. exit the dialog
        Button cancelButton = (Button) rootView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        //onClickListener for action depending on what time was selected
        Button setReminderButton = (Button) rootView.findViewById(R.id.setReminderButton);
        setReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((BlocNotes) getActivity()).setAlertAlarm(mReminderKey, mNoteText, mNoteId);
                dismiss();
            }
        });

        return rootView;
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
    public interface OnSetAlarmListener {
        public void setAlertAlarm(int reminderKey, String noteText, String noteId);
    }

}
