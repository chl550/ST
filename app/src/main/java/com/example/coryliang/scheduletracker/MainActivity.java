package com.example.coryliang.scheduletracker;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.icu.util.Output;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.location.Geocoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


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
                if (user.equals("care") && pass.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Successful Login",
                            Toast.LENGTH_LONG).show();
                    caregiverLaunch();
                    //move to caregiver screen here
                } else if (user.equals("pat") && pass.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Successful patient login",
                            Toast.LENGTH_LONG).show();
                    //move to patient screen here
                    patientLaunch();
                } else {
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

    private void patientLaunch() {
        Intent intent1 = new Intent(this, PatientActivity.class);
        this.startActivity(intent1);
    }


}
