package com.example.inahum.theshawshankredemption;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditScreen extends AppCompatActivity {

    DataBaseHelper mDataBaseHelper;
    private Button btnAdd;
    private EditText editText;
    private static final String TAG = "ListDataActivity";
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
                String description = editText.getText().toString();
                if(editText.length() != 0){
                    AddData("inbal", description);
                    populateListView();
                    editText.setText("");
                } else{
                    toastMessage("You must put something in the text field!");
                }
            }
        });

    }

    private void populateListView(){
    Log.d(TAG, "populateListView: Dispalying data in the list view." );
        Cursor data = mDataBaseHelper.getData();
        ArrayList<String> listData = new ArrayList<>();

        while(data.moveToNext()){ /****/
            listData.add(data.getString(1) + data.getString(2));
        }

        ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_layout, R.id.textView, listData);
        listView.setAdapter(adapter);
    }

    public void AddData(String title, String description){
        boolean insertData = mDataBaseHelper.addData(title, description);

        if(insertData){
            toastMessage("Data Successfully Inserted!");
        }else{
            toastMessage("Something Went Wrong!");
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
