package com.example.inahum.theshawshankredemption;

import android.Manifest;
import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SoundPool soundPool;
    private int startSoundId;
    private double lat;
    double lng;
    private ListView listView;
    private Button btnAddEdit;
    private DataBaseHelper mDataBaseHelper;
    private static final String TAG = "ListDataActivity";
    private FusedLocationProviderClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationClient = LocationServices
                .getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION},
                    1);
        } else {
            startGettingLocation();
        }

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

    @SuppressLint("MissingPermission")
    private void startGettingLocation() {
        locationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                            /**sound**/
                            if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                                AudioAttributes audioAttributes = new AudioAttributes.Builder()
                                        .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                        .build();
                                soundPool = new SoundPool.Builder()
                                        .setMaxStreams(10).setAudioAttributes(audioAttributes).build();
                            } else {
                                soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
                            }
                            startSoundId = soundPool.load(MainActivity.this, R.raw.magic, 1);
                            soundPool.play(startSoundId, 1, 1, 1, 0, 1f);
                            /**sound**/
                            if (Math.abs(lat - 33.237465) < 0.00005 && Math.abs(lng - 35.606734) < 0.00005) {
                                mDataBaseHelper.setStatusByLocation();
                                /**sound**/
                                if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
                                    AudioAttributes audioAttributes = new AudioAttributes.Builder()
                                            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                            .build();
                                    soundPool = new SoundPool.Builder()
                                            .setMaxStreams(10).setAudioAttributes(audioAttributes).build();
                                } else {
                                    soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
                                }
                                startSoundId = soundPool.load(MainActivity.this, R.raw.magic, 1);
                                soundPool.play(startSoundId, 1, 1, 1, 0, 1f);
                                /**sound**/
                            }
                            Toast.makeText(MainActivity.this,
                                    "lat: " + lat + ", lng: " + lng,
                                    Toast.LENGTH_LONG).show();
                            ((TextView) findViewById(R.id.textLocation)).setText(
                                    "lat: " + lat + ", lng: " + lng
                            );
                        }
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {

            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    startGettingLocation();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;

        }
    }

    private void populateListView() {
        Log.d(TAG, "populateListView: Dispalying data in the list view.");
        Cursor data = mDataBaseHelper.getData();
        ArrayList<Note> listNote = new ArrayList<>();

        while (data.moveToNext()) {
            listNote.add(new Note
                    (data.getString(1), data.getString(2), data.getString(3)));
        }
        data.close();

        listView.setAdapter(new NoteListAdapter(this, listNote, mDataBaseHelper));
    }

}
