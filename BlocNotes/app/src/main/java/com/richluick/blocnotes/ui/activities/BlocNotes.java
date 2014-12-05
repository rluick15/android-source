package com.richluick.blocnotes.ui.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Display;
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
import com.richluick.blocnotes.ui.fragments.ImageUrlFragment;
import com.richluick.blocnotes.ui.fragments.NavigationDrawerFragment;
import com.richluick.blocnotes.ui.fragments.NoteBookFragment;
import com.richluick.blocnotes.ui.fragments.SetReminderFragment;
import com.richluick.blocnotes.ui.fragments.SettingsFragment;
import com.richluick.blocnotes.utils.Constants;
import com.richluick.blocnotes.utils.ReminderReceiver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;


public class BlocNotes extends FragmentActivity implements CustomStyleDialogFragment.OnFragmentInteractionListener,
        NavigationDrawerFragment.NavigationDrawerCallbacks, AddNotebookFragment.OnFragmentInteractionListener,
        NoteAdapter.OnNoteBookAdapterListener, SetReminderFragment.OnSetAlarmListener, ImageUrlFragment.OnSetUrlInteractionListener {

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

        //handle incoming intents
        incomingIntent();

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

        Display display = getWindowManager().getDefaultDisplay();
        float frameRate = display.getRefreshRate();

        //set user selected SharedPreferences or default settings if never set
        SharedPreferences sharedPrefs = getPreferences(0);
        sharedPrefs.edit().putBoolean("enable_animations", frameRate > 40f).commit();
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
     * This method is called when the activity is first created and is responsible for handling
     * incoming intents to the main activity. It checks if intent is null and if not, executes
     * code depending on the action constant of the intent. This was moved from onCreate primarily
     * for organizational and "cleanliness" purposes.
     * */
    private void incomingIntent() {
        Intent intent = getIntent();
        if(intent != null && (intent.getAction()).equals(Constants.ACTION_CONTENT)) {
            String intentId = intent.getStringExtra(Constants.KEY_NOTE_ID);
            String intentBody = intent.getStringExtra(Constants.KEY_NOTE_BODY);
            int notebookNumber = intent.getIntExtra(Constants.KEY_NOTEBOOK_NUMBER, 0);
            editTextDialog(intentBody, intentId);
            intent.setAction(Constants.ACTION_BLANK);
            if(mNavigationDrawerFragment != null) {
                goToNotebook(notebookNumber);
            }
        }

        //dismiss the notification(if relevant) when an option is selected
        if (Context.NOTIFICATION_SERVICE != null) {
            String ns = Context.NOTIFICATION_SERVICE;
            NotificationManager nMgr = (NotificationManager) getApplicationContext().getSystemService(ns);
            nMgr.cancel(Constants.KEY_INTENT_REQUEST_CODE_ZERO);
        }
    }

    /**
     * This method is called when a new NoteBook fragment is opened. It runs on a background thread
     * and retrieves a writable database and uses it to run a query and store that into a
     * cursor object. It then calls a method to populate the notebook screen with all the notes
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

    /**
     * This method launches a dialog fragment and allows the user to edit the selected note
     *
     * @param noteText the current text in the note body
     * @param noteId the id of the note in the database table
     * */
    @Override
    public void editTextDialog(String noteText, String noteId) {
        FragmentManager fm = getSupportFragmentManager();
        EditNoteFragment editNoteFragment = new EditNoteFragment(noteText, noteId);
        editNoteFragment.show(fm, "dialog");
    }

    /**
     * This method launches a dialog fragment and allows the user to set an alarm reminder for a
     * specific note
     *  @param noteText the current text in the note body
     * @param noteId the id of the note in the database table
     * @param notebookNumber the id number corresponding to the notebook
     * */
    public void setReminderDialog(String noteText, String noteId, int notebookNumber) {
        FragmentManager fm = getSupportFragmentManager();
        SetReminderFragment reminderFragment = new SetReminderFragment(noteText, noteId, notebookNumber);
        reminderFragment.show(fm, "dialog");
    }

    /**
     * This method is called from the reminder dialog and sets a reminder alarm based upon
     * the time the user selected
     *
     * @param noteText the current text in the note body
     * @param noteId the id of the note in the database table
     * */
    @Override
    public void setAlertAlarm(int reminderKey, String noteText, String noteId, int notebookNumber) {
        int alertTime = 0;
        switch(reminderKey) {
            case 0:
                alertTime = 5;
                break;
            case 1:
                alertTime = 10;
                break;
            case 2:
                alertTime = 30;
                break;
            case 3:
                alertTime = 60;
                break;
        }

        Intent reminderReceiverIntent = new Intent(this, ReminderReceiver.class);
        reminderReceiverIntent.setAction(Constants.ACTION_SHOW_NOTIFICATION);
        reminderReceiverIntent.putExtra(Constants.KEY_NOTE_BODY, noteText);
        reminderReceiverIntent.putExtra(Constants.KEY_NOTE_ID, noteId);
        reminderReceiverIntent.putExtra(Constants.KEY_NOTEBOOK_NUMBER, notebookNumber);
        PendingIntent reminderPendingIntent = PendingIntent.getBroadcast(this, 0, reminderReceiverIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + alertTime * 60 * 1000,
                reminderPendingIntent);
        Toast.makeText(this, getString(R.string.alarm_set_text), Toast.LENGTH_SHORT).show();
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
    public void setImageUrl(String noteId) {
        FragmentManager fm = getSupportFragmentManager();
        ImageUrlFragment imageUrlFragment = new ImageUrlFragment(noteId);
        imageUrlFragment.show(fm, "dialog");
    }

    @Override
    public void saveImageUrl(final String urlText, final String noteId) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                mNotesTable.addImageUrl(mDb, urlText, noteId);
                //download the image and save to SD
                Bitmap urlImage = null;
                try {
                    URL url = new URL(urlText);
                    URLConnection urlConnection = url.openConnection();
                    InputStream inputStream = urlConnection.getInputStream();
                    urlImage = BitmapFactory.decodeStream(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final boolean success = saveImageToSD(urlImage, noteId);
                int notebookNumber = mNoteBookFragment.getNotebookNumber();
                final Cursor cursor = mNotesTable.notesQuery(mDb, notebookNumber);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (success == true) {
                            mNoteBookFragment.refreshNoteList(cursor);
                            Toast.makeText(BlocNotes.this,
                                    getString(R.string.toast_image_url_added), Toast.LENGTH_LONG).show();
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle(getString(R.string.error_title))
                                    .setMessage(getString(R.string.error_bad_url_no_sd))
                                    .setPositiveButton(getString(android.R.string.ok), null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    }
                });
            }
        }.start();
    }

    private boolean saveImageToSD(Bitmap image, String noteId) {
        if (image == null) {
            return false;
        }

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = null;
            ByteArrayOutputStream imageBytes = null;
            String name = null;

            boolean doesExist = true;
            while (doesExist == true) {
                Random generator = new Random();
                int n = 100000;
                n = generator.nextInt(n);
                name = "Image-" + n + ".jpg";

                imageBytes = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, imageBytes);
                File extCache = getExternalCacheDir();
                file = new File(extCache.getAbsolutePath()
                        + File.separator + name);

                if (file.exists()) file.delete();
                else doesExist = false;
            }
            mNotesTable.addImageName(mDb, name, noteId);

            try {
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(imageBytes.toByteArray());
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }
        return false;
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
            goToNotebook(adjustedNumber);
        }
    }

    private void goToNotebook(int notebookNumber) {
        SimpleCursorAdapter cursorAdapter = mNavigationDrawerFragment.getCursorAdapter();
        if(cursorAdapter != null) {
            Cursor cursor = (Cursor) cursorAdapter.getItem(notebookNumber);
            //set the page title to the specific notebooks name
            mTitle = cursor.getString(
                    cursor.getColumnIndex(Constants.TABLE_COLUMN_NOTEBOOK_NAME));

            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.container, new NoteBookFragment(notebookNumber))
                    .commit();
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
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.card_flip_right_in, R.anim.card_flip_right_out,
                            R.anim.card_flip_left_in, R.anim.card_flip_left_out)
                    .replace(R.id.container, settingsFragment)
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