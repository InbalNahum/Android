package com.example.inahum.theshawshankredemption;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditScreen extends AppCompatActivity {

    DataBaseHelper mDataBaseHelper;
    private Button btnAdd;
    private EditText editText;
    private static final String TAG = "EditScreen";
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_screen);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        editText = (EditText) findViewById(R.id.editText);
        mDataBaseHelper = new DataBaseHelper(this);
        listView = findViewById(R.id.listView);
        populateListView();

        btnAdd.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String editTextInput = editText.getText().toString();
                String[] parts = editTextInput.split(",");
                String title = parts[0];
                String description = parts[1];

                if (editText.length() != 0 && title.length() != 0 && description.length() != 0) {
                    AddData(title, description);
                    populateListView();
                    editText.setText("");
                } else {
                    toastMessage("You must put something in the text field, in format: Title, description ");
                }
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

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Note note =  (Note)adapterView.getItemAtPosition(i);
                Log.d(TAG, "onItemClick: You clicked on " + note.title);
                Cursor data = mDataBaseHelper.getItemId(note.title); //get id of item
                int itemId = -1;
                String status = "";
                while (data.moveToNext()) {
                    itemId = data.getInt(0);
                    status = data.getString(3);
                }
                data.close();
                if(status.equals("Received")){
                    return;
                }
                if (itemId > -1) {
                    Log.d(TAG, "onItemClick: The ID is: " + itemId);
                    Intent editScreenIntent = new Intent(EditScreen.this, EditDataActivity.class);
                    editScreenIntent.putExtra("id", itemId);
                    editScreenIntent.putExtra("title", note.title);
                    editScreenIntent.putExtra("description", note.description);
                    startActivity(editScreenIntent);
                } else {
                    toastMessage("No ID aaaociated with that title");
                }
            }
        });
    }

    public void AddData(String title, String description) {
        boolean insertData = mDataBaseHelper.addData(title, description);

        if (insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something Went Wrong!");
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
