package com.example.coryliang.scheduletracker;

import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.location.Geocoder;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button login = (Button) findViewById(R.id.login);
        final EditText username = (EditText) findViewById(R.id.username);
        final EditText password = (EditText) findViewById(R.id.password);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String user = username.getText().toString();
                final String pass = password.getText().toString();
                System.out.println("THIS IS " + user);
                System.out.println("THIS IS " + pass);
                if (user.equals("caregiver") && pass.equals("pass")) {
                    Toast.makeText(getApplicationContext(), "Successful Login",
                            Toast.LENGTH_LONG).show();
                    caregiverLaunch();
                    //move to caregiver screen here
                }
                else if (user.equals("patient") && pass.equals("patientpass")) {
                    Toast.makeText(getApplicationContext(), "Successful patient login",
                            Toast.LENGTH_LONG).show();
                    //move to patient screen here
                }
                else {
                    Toast.makeText(getApplicationContext(), "Username and Password doesn't match",
                            Toast.LENGTH_LONG).show();
                    //keep in the same screen
                }
            }
        });
        //TODO: Find way to get signals from Arduino, then move into method to get location.

    }
    private void caregiverLaunch() {
        Intent intent = new Intent(this, CaregiverActivity.class);
        startActivity(intent);
    }

    public String findLocation(double longitude, double latitude) throws IOException {
        Geocoder code = new Geocoder(getApplicationContext());
        ArrayList<Address> currLocation = (ArrayList<Address>) code.getFromLocation(latitude, longitude, 1);
        Address address = currLocation.get(0);
        String retAddress = address.getAddressLine(0);
        return retAddress;
    }
}
