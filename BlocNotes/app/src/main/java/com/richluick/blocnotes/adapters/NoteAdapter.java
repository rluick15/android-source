package com.richluick.blocnotes.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.richluick.blocnotes.R;
import com.richluick.blocnotes.ui.activities.BlocNotes;
import com.richluick.blocnotes.ui.fragments.NoteBookFragment;
import com.richluick.blocnotes.utils.Constants;

public class NoteAdapter extends CursorAdapter implements PopupMenu.OnMenuItemClickListener {

    private Context mContext;
    private Cursor cursor;
    private final LayoutInflater inflater;
    private ImageButton mThreeDots;
    private PopupMenu mPopupMenu;
    private String mNoteText;
    private int mNotebookNumber;
    private String mNoteId;

    private View mView;
    private ViewGroup mParent;
    private NoteBookFragment mNotebookFragment;


    public NoteAdapter(Context context, Cursor cursor, LayoutInflater inflater, int notebookNumber, NoteBookFragment noteBookFragment) {
        super(context, cursor);
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.cursor = cursor;
        this.mNotebookNumber = notebookNumber;
        this.mNotebookFragment = noteBookFragment;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        mParent = parent;
        mView = inflater.inflate(R.layout.note_adapter, null);
        ViewHolder holder = new ViewHolder();
        holder.body = (TextView) mView.findViewById(R.id.noteText);
        holder.id = (TextView) mView.findViewById(R.id.idHolder);
        mView.setTag(holder);

        return mView;
    }

    @Override
    public void bindView(View view, Context context, final Cursor cursor) {
        final ViewHolder holder = (ViewHolder) view.getTag();

        String noteBody = cursor.getString(cursor.getColumnIndex(Constants.TABLE_COLUMN_NOTES_BODY));
        String noteId = cursor.getString(cursor.getColumnIndex(Constants.TABLE_COLUMN_ID));

        holder.body.setText(noteBody);
        holder.id.setText(noteId);

        //setup popup Menu
        mPopupMenu = new PopupMenu(mContext, mThreeDots);
        mPopupMenu.getMenu().add(Menu.NONE, 0, Menu.NONE, "Edit Note");
        mPopupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "Set Reminder");
        mPopupMenu.getMenu().add(Menu.NONE, 2, Menu.NONE, "Delete Note");
        mPopupMenu.setOnMenuItemClickListener(this);

        mThreeDots = (ImageButton) mView.findViewById(R.id.threeDots);
        mThreeDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNoteText = holder.body.getText().toString();
                mNoteId = holder.id.getText().toString();
                mPopupMenu.show();
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
        case 0:
            ((BlocNotes) mContext).editTextDialog(mNoteText, mNoteId);
            break;
        case 1:
            ((BlocNotes) mContext).setReminderDialog(mNoteText, mNoteId, mNotebookNumber);
            break;
        case 2:
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle(mContext.getString(R.string.delete_note_title))
                    .setMessage(mContext.getString(R.string.delete_note_message))
                    .setNegativeButton(android.R.string.cancel, null)
                    .setPositiveButton(mContext.getString(R.string.delete_note_button_text), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ((BlocNotes) mContext).deleteNote(mNoteId);
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            break;
    }
        return false;
    }

    /**
     * This is a setter method for setting the font the user has selected from the spinner.
     *
     * param font the font object the user selected passed on from the NoteBookFragment
     * */
    public void setCustomFont(Typeface font) {
        ListView lv = (ListView) mParent;
        for(int i = 0; i < lv.getChildCount(); i++) {
            TextView note = (TextView) lv.getChildAt(i).findViewById(R.id.noteText);
            note.setTypeface(font);
        }
    }

    /**
     * This is a setter method for setting the font style the user has selected from custom menu
     *
     * param styleId the integer id of the font stlye selected (SMALL, MEDIUM, LARGE)
     * */
    public void setCustomStyle(int styleId) {
        ViewHolder holder = (ViewHolder) mView.getTag();
        if (styleId == 1) {
            holder.body.setTextAppearance(mContext, android.R.style.TextAppearance_Small);
        }
        else if (styleId == 2) {
            holder.body.setTextAppearance(mContext, android.R.style.TextAppearance_Medium);
        }
        else if (styleId == 3) {
            holder.body.setTextAppearance(mContext, android.R.style.TextAppearance_Large);
        }

    }

    private static class ViewHolder {
        TextView body;
        TextView id;
    }

    public interface OnNoteBookAdapterListener {
        public void editTextDialog(String noteText, String noteId);
        public void setReminderDialog(String noteText, String noteId, int notebookNumber);
        public void deleteNote(String noteId);
    }
}
