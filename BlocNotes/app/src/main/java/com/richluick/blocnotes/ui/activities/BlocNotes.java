package com.richluick.blocnotes.ui.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.richluick.blocnotes.BlocNotesApplication;
import com.richluick.blocnotes.R;
import com.richluick.blocnotes.adapters.NoteAdapter;
import com.richluick.blocnotes.database.tables.NotebooksTable;
import com.richluick.blocnotes.database.tables.NotesTable;
import com.richluick.blocnotes.ui.fragments.AddNotebookFragment;
import com.richluick.blocnotes.ui.fragments.CustomStyleDialogFragment;
import com.richluick.blocnotes.ui.fragments.EditNoteFragment;
import com.richluick.blocnotes.ui.fragments.NavigationDrawerFragment;
import com.richluick.blocnotes.ui.fragments.NoteBookFragment;
import com.richluick.blocnotes.ui.fragments.SetReminderFragment;
import com.richluick.blocnotes.ui.fragments.SettingsFragment;
import com.richluick.blocnotes.utils.Constants;
import com.richluick.blocnotes.utils.ReminderReceiver;


public class BlocNotes extends FragmentActivity implements CustomStyleDialogFragment.OnFragmentInteractionListener,
        NavigationDrawerFragment.NavigationDrawerCallbacks, AddNotebookFragment.OnFragmentInteractionListener,
        NoteAdapter.OnNoteBookAdapterListener, SetReminderFragment.OnSetAlarmListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private NoteBookFragment mNoteBookFragment;

    private SQLiteDatabase mDb;
    private Context mContext;

    private NotebooksTable mNotebooksTable = new NotebooksTable();
    private NotesTable mNotesTable = new NotesTable();

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloc_notes);
        mContext = this;

        //get a writable database to use throughout the app
        new Thread() {
            @Override
            public void run() {
                super.run();
                mDb = BlocNotesApplication.get(mContext).getWritableDb();
            }
        }.start();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

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
            setNoteBookFragment();
        } catch(ClassCastException ignored) {}
        if (mNoteBookFragment == null) {
            mNoteBookFragment = new NoteBookFragment(0);
            getFragmentManager().beginTransaction().replace(R.id.container, mNoteBookFragment).commit();
            getFragmentManager().executePendingTransactions();
        }

        //set user selected SharedPreferences or default settings if never set
        SharedPreferences sharedPrefs = getPreferences(0);
        int stylePref = sharedPrefs.getInt(Constants.PREF_FONT_SIZE, 2);
        String fontPref = sharedPrefs.getString(Constants.PREF_TYPEFACE, "");
        onStyleChange(null , stylePref);
        onFontChange(null, fontPref);

        setSharedPrefs();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mDb != null) {
            mDb.close(); //close the database
        }
    }

    /**
     * This method is called when a new NoteBook fragment is opened. It runs on a background thread
     * and retrieves a writable database and uses it to run a query and store that into a
     * cursor object. It then call a method to populate the notebook screen with all the notes
     * assigned to that notebook
     * */
    public void getNotebookCursorInBackground() {
        new Thread() {
            @Override
            public void run() {
                super.run();

                int notebookNumber = mNoteBookFragment.getNotebookNumber();
                final Cursor cursor = mNotesTable.notesQuery(mDb, notebookNumber);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mNoteBookFragment.setNotebookAdapter(cursor);
                    }
                });
            }
        }.start();
    }

    /**
     * This method is called when a new note is added to the notebook. It adds the text to the
     * writable database opened when the Notebook was opened and then calls a method to
     * update the UI with the new list of notes
     *
     * @param noteText
     * @param notebookNumber
     */
    public void createNewNote(final String noteText, final int notebookNumber) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                mNotesTable.addNewNote(mDb, noteText, notebookNumber);
                int notebookNumber = mNoteBookFragment.getNotebookNumber();
                final Cursor cursor = mNotesTable.notesQuery(mDb, notebookNumber);
                BlocNotes.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mNoteBookFragment.refreshNoteList(cursor);
                    }
                });
            }
        }.start();
    }

    /**
     * This method is called when a new notebook is created by the user. It adds it to the
     * database then recreates the cursor query and then calls a method to update the
     * Cursor Adapter in the navigation drawer
     *
     * @param newTitle This is the new Title for the notebook the user has created
     */
    @Override
    public void addNotebook(final String newTitle) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                mNotebooksTable.addNotebook(mDb, newTitle);
                //get the new cursor and update the database
                final Cursor cursor = mNavigationDrawerFragment.getCursor(mDb);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mNavigationDrawerFragment.updateDatabase(cursor);
                    }
                });
            }
        }.start();
    }

    /**
     * This method sets the variable mNoteBookFragment to hold the current Notebook fragment
     * being displayed on the screen. It is called onCreate of the Main Activity as well as from
     * the fragments onCreateView to ensure it is changed upon opening of the new fragment
     * */
    public void setNoteBookFragment() {
        mNoteBookFragment = (NoteBookFragment) getFragmentManager().findFragmentById(R.id.container);
    }

    /**
     * This method sets the shared prefs based on the default preferences designated by the user
     * in the SettingsFragment. It is called onCreate of the Main Activity and also from the
     * onResumeMethod in the NoteFragment. This allows for an immediate setting change when the
     * user returns from the settings fragment to the NoteFragment.
     * */
    public void setSharedPrefs() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String fontPreference = sharedPreferences.getString(Constants.PREF_TYPEFACE, "");
        int stylePreference = Integer.parseInt(sharedPreferences.getString(Constants.PREF_FONT_SIZE, "2"));
        onStyleChange(null, stylePreference);
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
        mNoteBookFragment.setCustomStyle(styleId);
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
        mNoteBookFragment.setCustomFont(fontName);
    }

    @Override
    public void onThemeChange(CustomStyleDialogFragment dialog, int themeId) {}


    @Override
    public void editTextDialog(String noteText, String noteId) {
        FragmentManager fm = getSupportFragmentManager();
        EditNoteFragment editNoteFragment = new EditNoteFragment(noteText, noteId);
        editNoteFragment.show(fm, "dialog");
    }

    public void setReminderDialog(String noteText, String noteId) {
        FragmentManager fm = getSupportFragmentManager();
        SetReminderFragment reminderFragment = new SetReminderFragment(noteText, noteId);
        reminderFragment.show(fm, "dialog");
    }

    @Override
    public void setAlertAlarm(int reminderKey, String noteText, String noteId) {
        int alertTime;



        Intent reminderReceiverIntent = new Intent(this, ReminderReceiver.class);
        reminderReceiverIntent.setAction("SHOW_NOTIFICATION");
        PendingIntent reminderPendingIntent = PendingIntent.getBroadcast(this, 0, reminderReceiverIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC,
                System.currentTimeMillis() + 5 * 60 * 1000,
                reminderPendingIntent);
    }

    public void updateDatabaseNewText(final String updatedNote, final String noteId) {
        new Thread() {
            @Override
            public void run() {
                super.run();

                mNotesTable.editNote(mDb, updatedNote, noteId);
                int notebookNumber = mNoteBookFragment.getNotebookNumber();
                final Cursor cursor = mNotesTable.notesQuery(mDb, notebookNumber);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mNoteBookFragment.refreshNoteList(cursor);
                        Toast.makeText(BlocNotes.this,
                                getString(R.string.update_note_toast), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }.start();
    }

    @Override
    public void deleteNote(final String noteId) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                mNotesTable.deleteNote(mDb, noteId);
                int notebookNumber = mNoteBookFragment.getNotebookNumber();
                final Cursor cursor = mNotesTable.notesQuery(mDb, notebookNumber);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mNoteBookFragment.refreshNoteList(cursor);
                        Toast.makeText(BlocNotes.this,
                                getString(R.string.delete_note_toast), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }.start();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        if(mNavigationDrawerFragment != null) {
            int adjustedNumber = number - 1;
            SimpleCursorAdapter cursorAdapter = mNavigationDrawerFragment.getCursorAdapter();
            if(cursorAdapter != null) {
                Cursor cursor = (Cursor) cursorAdapter.getItem(adjustedNumber);
                //set the page title to the specific notebooks name
                mTitle = cursor.getString(
                        cursor.getColumnIndex(Constants.TABLE_COLUMN_NOTEBOOK_NAME));

                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new NoteBookFragment(adjustedNumber))
                        .commit();
            }
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
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
        if (id == R.id.action_erase) {
            mNoteBookFragment.setNewNoteText("");
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
            return inflater.inflate(R.layout.fragment_bloc_notes, container, false);
        }

        @Override
        public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((BlocNotes) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}