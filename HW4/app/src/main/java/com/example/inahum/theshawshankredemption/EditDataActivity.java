package com.example.inahum.theshawshankredemption;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by inahum on 5/5/2018.
 */

public class EditDataActivity extends AppCompatActivity {

    private static final String TAG = "EditDataActivity";

    private Button btnSave, btnDelete;
    private EditText editTitle, editDescription;
    private int selectId;
    private String selectTitle;
    private String selectDescription;
    DataBaseHelper mDataBaseHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data_layout);
        btnSave = findViewById(R.id.btnSave);
        btnDelete = findViewById(R.id.btnDelete);
        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        mDataBaseHelper = new DataBaseHelper(this);

        //get the intent extra from EditScreen
        Intent receiveIntent = getIntent();
        selectId = receiveIntent.getIntExtra("id", -1);
        selectTitle = receiveIntent.getStringExtra("title");
        selectDescription = receiveIntent.getStringExtra("description");
        //set the text to show the current selected item
        editTitle.setText(selectTitle);
        editDescription.setText(selectDescription);

        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTitle.getText().toString();
                String description = editDescription.getText().toString();

                if (title.length() != 0 && description.length() != 0) {
                    mDataBaseHelper.updateNote(selectId, title, selectTitle, description, selectDescription);
                    Intent editScreenIntent = new Intent(EditDataActivity.this, EditScreen.class);
                    startActivity(editScreenIntent);
                    toastMessage("Saved on dataBase");

                } else {
                    toastMessage("You must enter a format: title, name");
                }
            }
        });

        btnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                mDataBaseHelper.deleteNote(selectId, selectTitle);
                editTitle.setText("");
                editDescription.setText("");
                Intent editScreenIntent = new Intent(EditDataActivity.this, EditScreen.class);
                startActivity(editScreenIntent);
                toastMessage("Remove from dataBase");
            }
        });
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
