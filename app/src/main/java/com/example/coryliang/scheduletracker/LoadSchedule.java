package com.example.coryliang.scheduletracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class LoadSchedule extends AppCompatActivity {
    Calendar calendar = new GregorianCalendar();
    Schedule schedule = new Schedule();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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




    }

}
