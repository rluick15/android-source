package com.richluick.blocnotes.ui.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.richluick.blocnotes.R;


/**
 * This is a Preference Settings Fragment used for defining the user preferences for the
 * application. *
 */
public class SettingsFragment extends PreferenceFragment {

    public SettingsFragment() {} // Required empty public constructor

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fragment_settings);
    }
}
