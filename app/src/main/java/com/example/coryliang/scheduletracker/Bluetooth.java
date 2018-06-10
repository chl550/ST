package com.example.coryliang.scheduletracker;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import static java.lang.Float.valueOf;
import static java.security.AccessController.getContext;

/**
 * Created by Cory Liang on 6/5/2018.
 */

public class Bluetooth extends Thread implements Runnable{
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private final BluetoothAdapter blue;
    BluetoothSocket socket = null;
    InputStream input = null;
    OutputStream output = null;
    CaregiverActivity activity = null;
    Handler care = new Handler(Looper.myLooper());

    public Bluetooth(BluetoothAdapter blue, CaregiverActivity activity) {
        this.blue = blue;
        this.activity = activity;
    }

    public boolean connect(BluetoothAdapter blue) {
        Set bond = blue.getBondedDevices();
        if (bond.isEmpty()) {
            Log.d("Blue","No Paired Devices");
            return false;

        }
        BluetoothDevice hc = null;
        boolean found = false;
        for (BluetoothDevice pair: blue.getBondedDevices()) {
            if (pair.getAddress().equals("00:14:03:06:68:12")) {
                hc = pair;
                found = true;
                Log.d("Blue", "Found device");

                break;
            }
        }

        try {
            socket = hc.createRfcommSocketToServiceRecord(MY_UUID);
            socket.connect();
            input = socket.getInputStream();
            output = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Log.d("Blue", "Connected");
        return true;
    }
    //send a message to arduino
    public void send(String message) throws IOException {
        output.write(message.getBytes());
    }
    @Override
    public void run() {
        String combine = "";
        float latitude = 0;
        float longitude = 0;
        boolean check = true;
        if (connect(blue) == true) {
            while(check) {
                int byteCount = 0;
                if (Thread.interrupted()) {
                    break;
                }
                try {
                    byteCount = input.available();
                    if (byteCount > 0) {
                        byte[] rawBytes = new byte[byteCount];
                        input.read(rawBytes);
                        final String string = new String(rawBytes, "UTF-8");
                        Log.d("READ", string);

                        combine += string;
                        //combine the strings
                        if (combine.contains("#")) {
                            String foo[];
                            foo = combine.split("\n");
                            combine = foo[0].replaceAll("#","");
                            Log.d("READ", "Combine has " + combine + " " +combine.length());
                            if (combine.length() > 15) {
                            String[] array = combine.split(" ");
                            combine="";
                                latitude = Float.valueOf(array[0]);
                                longitude = Float.valueOf(array[1]);
                                final float finalLatitude = latitude;
                                final float finalLongitude = longitude;
                                Runnable run = new Runnable() {
                                    @Override
                                    public void run() {
                                        activity.updateLocation(finalLatitude, finalLongitude);
                                        try {
                                            activity.findLocation(finalLatitude, finalLongitude);
                                            activity.checkList();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                };
                                care.post(run);
                            }

                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
