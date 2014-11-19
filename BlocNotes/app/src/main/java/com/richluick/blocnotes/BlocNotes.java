package com.richluick.blocnotes;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class BlocNotes extends FragmentActivity implements CustomStyleDialogFragment.OnFragmentInteractionListener,
        NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private NoteFragment mNoteFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloc_notes);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
     protected void onStart() {
        super.onStart();

        //create a new note fragment if one has not been created yet
        try {
            mNoteFragment = (NoteFragment) getFragmentManager().findFragmentById(R.id.container);
        }
        catch(ClassCastException e) {}
        if (mNoteFragment == null) {
            mNoteFragment = new NoteFragment();
            getFragmentManager().beginTransaction().replace(R.id.container, mNoteFragment).commit();
            getFragmentManager().executePendingTransactions();
        }

        //set user selected SharedPreferences or default settings if never set
        SharedPreferences sharedPrefs = getPreferences(0);
        int stylePref = sharedPrefs.getInt(SharedPreferanceConstants.PREF_FONT_SIZE, 2);
        String fontPref = sharedPrefs.getString(SharedPreferanceConstants.PREF_TYPEFACE, "");
        onStyleChange(null , stylePref);
        onFontChange(null, fontPref);

        setSharedPrefs();
    }

    /**
     * This method sets the shared prefs based on the default preferences designated by the user
     * in the SettingsFragment. It is called onCreate of the Main Activity and also from the
     * onResumeMethod in the NoteFragment. This allows for an immediate setting change when the
     * user returns from the settings fragment to the NoteFragment.
     * */
    public void setSharedPrefs() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String fontPreference = sharedPreferences.getString(SharedPreferanceConstants.PREF_TYPEFACE, "");
        int stylePreference = Integer.parseInt(sharedPreferences.getString(SharedPreferanceConstants.PREF_FONT_SIZE, "2"));
        onStyleChange(null , stylePreference);
        onFontChange(null, fontPreference);
    }

    /**
     * This method changes the typeface on the NoteFragment based on the users choice in the
     * spinner from the custom dialog.
     *
     * @param dialog instance of CustomStyleDialog the user opened to change the font size
     * @param styleId the int id value corresponding to a font size
     * */
    @Override
    public void onStyleChange(CustomStyleDialogFragment dialog, int styleId) {
        mNoteFragment.setCustomStyle(styleId);
    }

    /**
     * This method changes the typeface on the NoteFragment based on the users choice in the
     * spinner from the custom dialog.
     *
     * @param dialog instance of CustomeStyleDialog the user opened to change the font
     * @param fontName the name of the font the user selected
     * */
    @Override
    public void onFontChange(CustomStyleDialogFragment dialog, String fontName) {
        mNoteFragment.setCustomFont(fontName);
    }

    @Override
    public void onThemeChange(CustomStyleDialogFragment dialog, int themeId) {}

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.bloc_notes, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_custom_style) {
            FragmentManager fm = getSupportFragmentManager();
            CustomStyleDialogFragment customStyleDialogFragment = new CustomStyleDialogFragment();
            customStyleDialogFragment.show(fm, "dialog");
        }
        if (id == R.id.action_settings) {
            SettingsFragment settingsFragment = new SettingsFragment();
            getFragmentManager().beginTransaction().replace(R.id.container, settingsFragment)
                    .addToBackStack(null).commit();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is used to handle back button presses on Fragments
     */
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }
        public PlaceholderFragment() {}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_bloc_notes, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((BlocNotes) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}