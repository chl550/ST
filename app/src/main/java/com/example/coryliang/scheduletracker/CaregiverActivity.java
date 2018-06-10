package com.example.coryliang.scheduletracker;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Stack;

public class CaregiverActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    @Expose
    private Schedule schedule = new Schedule();
    Calendar calendar = new GregorianCalendar();
    public String currLocation = "";
    private Stack<Marker> markers = new Stack<Marker>();
    ListAdapter listAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //for demo and testing purposes only, remove once demo is done. Tests adding to schedule
        //need to add a schedule loader, another activity or class
        calendar.set(2018, 9, 24, 5, 0 , 0);
        schedule.addTask(calendar.getTimeInMillis(), "Washington", "Water the lawn");
        calendar.set(2018, 9, 24, 6, 0 , 0);
        schedule.addTask(calendar.getTimeInMillis(), "California", "Fix IT");
        calendar.set(2018, 9, 24, 7, 0 , 0);
        schedule.addTask(calendar.getTimeInMillis(), "Missouri", "Cook food");

        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor prefEdit = pref.edit();
        //bugged information isnt passing to patient activity
        GsonBuilder g = new GsonBuilder();
        Gson gson = g.create();
        String json = gson.toJson(schedule.getMap());
        Log.d("json", json);
        prefEdit.putString("schedule", json);
        prefEdit.commit();
        ListView list = (ListView) findViewById(R.id.taskList);
        listAdapter = new ListAdapter(getApplicationContext(), schedule, 1);
        list.setAdapter(listAdapter);
        //start bluetooth connection
        BluetoothAdapter blue = BluetoothAdapter.getDefaultAdapter();
        if (blue == null) {
            Log.d("Blue","No bluetooth");

        }
        else if (!blue.isEnabled()) {
            Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enable,0);
        }
        final Bluetooth thread = new Bluetooth(blue, this);
        Log.d("Blue", "running thread");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                thread.start();
            }
        });
        Log.d("Blue", "ran thread");

    }
    @Override
    public void onResume() {
        super.onResume();
        listAdapter.notifyDataSetChanged();
        Log.d("Update", "Updating List");
    }
    public void checkList() {
        String currTask;
        String currAddress;
        Long time;
        Long currTime = calendar.getTimeInMillis();
        //keep checking until schedule is empty
        while(!schedule.schedule.isEmpty()) {
            Log.d("Alert", "ALERT TRIGGERED");
            currTask = schedule.getNTask(0);
            currAddress = schedule.getNLocation(0);
            time = schedule.getNKey(0);
            if (!currLocation.equals(currAddress) && time == currTime || (time != currTime && schedule.schedule.get(time).getStatus() == false)) {
                //Move into Alert, send signal to arduino to buzz,
                Intent intent = new Intent(this, AlertActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Default Marker"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    //update map, move map marker.
    public void updateLocation(float latitude, float longitude) {
  /*     Marker top = null;
        if (!markers.empty()) {
            top = markers.pop();
        }
         top.remove();*/
        LatLng location = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(location).title("Moving Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    public void findLocation(double latitude, double longitude) throws IOException {
        Geocoder code = new Geocoder(getApplicationContext());
        ArrayList<Address> currLocation2 = (ArrayList<Address>) code.getFromLocation(latitude, longitude, 1);
        Address address = currLocation2.get(0);
        currLocation = address.getAddressLine(0);
        Log.d("Location", currLocation);

    }

}
