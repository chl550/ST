package com.example.coryliang.scheduletracker;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
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
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CaregiverActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Schedule schedule = new Schedule();
    Calendar calendar = new GregorianCalendar();
    public String currLocation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        calendar.set(2018, 9, 24, 5, 0 , 0);
        schedule.addTask(new Date(calendar.getTimeInMillis()), "Washington", "Water the lawn");
        calendar.set(2018, 9, 24, 6, 0 , 0);
        schedule.addTask(new Date(calendar.getTimeInMillis()), "California", "Fix IT");
        calendar.set(2018, 9, 24, 7, 0 , 0);
        schedule.addTask(new Date(calendar.getTimeInMillis()), "Missouri", "Cook food");
        ListView list = (ListView) findViewById(R.id.taskList);
        ListAdapter listAdapter = new ListAdapter(getApplicationContext(), schedule);
        list.setAdapter(listAdapter);
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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(32.715738, -117.16108400000002);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Default Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
    //update map.
    public void updateLocation(float latitude, float longitude) {
        LatLng location = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(location).title("Moving Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    public String findLocation(double longitude, double latitude) throws IOException {
        Geocoder code = new Geocoder(getApplicationContext());
        ArrayList<Address> currLocation = (ArrayList<Address>) code.getFromLocation(latitude, longitude, 1);
        Address address = currLocation.get(0);
        String retAddress = address.getAddressLine(0);
        return retAddress;
    }

}
