package com.example.inahum.theshawshankredemption;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by inahum on 5/4/2018.
 */


class NoteListAdapter extends ArrayAdapter<Note> {
    private ArrayList<Note> notes;
    private DataBaseHelper mDataBaseHelper;
    private static final String TAG = "NoteListAdapter";

    public NoteListAdapter(AppCompatActivity classApp, ArrayList<Note> notes, DataBaseHelper mDataBaseHelper) {
        super(classApp, R.layout.list_layout);
        this.notes = notes;
        this.mDataBaseHelper = mDataBaseHelper;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Note getItem(int i) {
        return notes.get(i);
    }

    @Override
    public View getView(int position,
                        View recycledView,
                        ViewGroup listView) {
        if (recycledView == null) {
            recycledView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_layout, null);
        }
        TextView title = recycledView.findViewById(R.id.title);
        TextView description = recycledView.findViewById(R.id.description);
        TextView status = recycledView.findViewById(R.id.status);

        Note note = getItem(position);
        Cursor data = mDataBaseHelper.getItemId(note.title); //get id of item
        int itemId = -1;
        while (data.moveToNext()) {
            itemId = data.getInt(0);
        }
        if (itemId > -1) {
            boolean isChange = mDataBaseHelper.setStatus(itemId);
            if (isChange == true) {
                status.setBackgroundColor(Color.RED);
            }
        }

        title.setText(note.title);
        description.setText(note.description);
        status.setText(note.status);

        return recycledView;
    }
}

