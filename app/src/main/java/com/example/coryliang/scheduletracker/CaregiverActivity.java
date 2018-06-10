package com.example.coryliang.scheduletracker;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Stack;

public class CaregiverActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    @Expose
    private Schedule schedule = null;
    Calendar calendar = new GregorianCalendar();
    public String currLocation = "";
    private Stack<Marker> markers = new Stack<Marker>();
    ListAdapter listAdapter = null;
    Map<Long, SchedulePair> hold = null;
    public static boolean check = false;
    Bluetooth thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_caregiver);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.d("check", String.valueOf(check));
        if (check == true) {
            Log.d("check", String.valueOf(check));
            SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
            GsonBuilder g = new GsonBuilder();
            Gson gson = g.create();
            String json = pref.getString("schedule", "");
            Log.d("json", json);
            Type type = new TypeToken<Map<Long, SchedulePair>>() {
            }.getType();
            hold = (Map<Long, SchedulePair>) gson.fromJson(json, type);
            schedule = new Schedule(hold);
            ListView list = (ListView) findViewById(R.id.taskList);
            listAdapter = new ListAdapter(getApplicationContext(), schedule, 1);
            list.setAdapter(listAdapter);
        }


        //start bluetooth connection
        BluetoothAdapter blue = BluetoothAdapter.getDefaultAdapter();
        if (blue == null) {
            Log.d("Blue","No bluetooth");

        }
        else if (!blue.isEnabled()) {
            Intent enable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enable,0);
        }
        thread = new Bluetooth(blue, this);
        Log.d("Blue", "running thread");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                thread.start();
            }
        });
        Log.d("Blue", "ran thread");
        final Button load = (Button) findViewById(R.id.load);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchLoad();
            }
        });

    }
    public void launchLoad() {
        Intent intent = new Intent(this,LoadSchedule.class);
        startActivityForResult(intent,200);
    }
    @Override
    public void onResume() {
        super.onResume();
        if (schedule != null && check == true) {
            SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
            GsonBuilder g = new GsonBuilder();
            Gson gson = g.create();
            String json = pref.getString("schedule", "");
            Log.d("json", json);
            Type type = new TypeToken<Map<Long, SchedulePair>>() {
            }.getType();
            hold = (Map<Long, SchedulePair>) gson.fromJson(json, type);
            schedule = new Schedule(hold);

            listAdapter.notifyDataSetChanged();
            Log.d("Update", "Updating List");
            try {
                checkList();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public void checkList() throws IOException {
        if (schedule != null) {
            Log.d("ALERT", "checking list");
            String currAddress;
            Long time;
            Long currTime = calendar.getTimeInMillis();
            //keep checking until schedule is empty
            if (schedule.checkAllTrue() == false) {
                //currLocation should hold gps location
                time = schedule.getNextFalse();
                if (schedule.schedule.get(time).getStatus() == false) {
                    //get the current location from the schedule
                    currAddress = schedule.schedule.get(time).getLocation();
                    //if location isn't the same
                    if (!currLocation.equals(currAddress) && time == currTime || (time != currTime && schedule.schedule.get(time).getStatus() == false)) {
                        //Move into Alert, send signal to arduino to buzz, should be popup
                        Log.d("Alert", "ALERT TRIGGERED");
                        String signal = "%";
                        //thread.send(signal);
                        /*
                        Intent intent = new Intent(this, AlertActivity.class);
                        startActivity(intent);
                        */
                    }
                }

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

    public void getSchedule() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        GsonBuilder g = new GsonBuilder();
        Gson gson = g.create();
        String json = pref.getString("schedule", "");
        Log.d("json", json);
        Type type = new TypeToken<Map<Long, SchedulePair>>() {
        }.getType();
        hold = (Map<Long, SchedulePair>) gson.fromJson(json, type);
        this.schedule = new Schedule(hold);
        ListView list = (ListView) findViewById(R.id.taskList);
        listAdapter = new ListAdapter(getApplicationContext(), schedule, 1);
        list.setAdapter(listAdapter);
        check = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getSchedule();
    }
    /*
    @Override
    public void onDestroy() {

        super.onDestroy();
        thread.interrupt();
    }
    */
}
