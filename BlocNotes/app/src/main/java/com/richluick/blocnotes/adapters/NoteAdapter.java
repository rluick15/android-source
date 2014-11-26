package com.richluick.blocnotes.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.richluick.blocnotes.R;
import com.richluick.blocnotes.ui.fragments.NoteBookFragment;
import com.richluick.blocnotes.utils.Constants;

public class NoteAdapter extends CursorAdapter {

    private Context mContext;
    private Cursor cursor;
    private final LayoutInflater inflater;
    private NoteBookFragment mNoteBookFragment;

    private View mView;

    public NoteAdapter(Context context, Cursor cursor, LayoutInflater inflater) {
        super(context, cursor);
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.cursor = cursor;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        mView = inflater.inflate(R.layout.note_adapter, null);
        ViewHolder holder = new ViewHolder();
        holder.body = (TextView) mView.findViewById(R.id.noteText);
        mView.setTag(holder);

        return mView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        String noteBody = cursor.getString(cursor.getColumnIndex(Constants.TABLE_COLUMN_NOTES_BODY));

        holder.body.setText(noteBody);
    }

//    /**
//     * This is a setter method for setting the font the user has selected from the spinner.
//     *
//     * param font the font object the user selected passed on from the NoteBookFragment
//     * */
//    public void setCustomFont(Typeface font) {
//        ViewHolder holder = (ViewHolder) mView.getTag();
//        holder.body.setTypeface(font);
//    }
//
//    /**
//     * This is a setter method for setting the font style the user has selected from custom menu
//     *
//     * param styleId the integer id of the font stlye selected (SMALL, MEDIUM, LARGE)
//     * */
//    public void setCustomStyle(int styleId) {
//        ViewHolder holder = (ViewHolder) mView.getTag();
//        if (styleId == 1) {
//            holder.body.setTextAppearance(mContext, android.R.style.TextAppearance_Small);
//        }
//        else if (styleId == 2) {
//            holder.body.setTextAppearance(mContext, android.R.style.TextAppearance_Medium);
//        }
//        else if (styleId == 3) {
//            holder.body.setTextAppearance(mContext, android.R.style.TextAppearance_Large);
//        }
//
//    }

    private static class ViewHolder {
        TextView body;
    }
}
