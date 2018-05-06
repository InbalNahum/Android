package com.example.inahum.theshawshankredemption;

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
    ArrayList<Note> notes;

    public NoteListAdapter(AppCompatActivity classApp, ArrayList<Note> notes) {
        super(classApp, R.layout.list_layout);
        this.notes = notes;
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

        Note n = getItem(position);
        title.setText(n.title);
        description.setText(n.description);
        status.setText(n.status);

        return recycledView;
    }
}

