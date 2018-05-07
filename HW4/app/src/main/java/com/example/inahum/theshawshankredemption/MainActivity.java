package com.example.inahum.theshawshankredemption;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Button btnAddEdit;
    DataBaseHelper mDataBaseHelper;
    private static final String TAG = "ListDataActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddEdit = (Button) findViewById(R.id.btnAddEdit);
        listView = (ListView) findViewById(R.id.listView);
        mDataBaseHelper = new DataBaseHelper(this);
        populateListView();

        btnAddEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditScreen.class);
                startActivity(intent);
            }
        });
    }

    private void populateListView() {

        Log.d(TAG, "populateListView: Dispalying data in the list view.");
        Cursor data = mDataBaseHelper.getData();
        ArrayList<Note> listNote = new ArrayList<>();

        while (data.moveToNext()) {
            listNote.add(new Note
                    (data.getString(1), data.getString(2), data.getString(3)));
        }

        listView.setAdapter(new NoteListAdapter(this, listNote, mDataBaseHelper));
    }

}
