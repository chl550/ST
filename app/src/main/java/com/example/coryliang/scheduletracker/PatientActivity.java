package com.example.coryliang.scheduletracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

public class PatientActivity extends AppCompatActivity {
    Schedule schedule = null;
    Map<Long, SchedulePair> hold = null;
    ListAdapter listAdapter1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
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


        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        GsonBuilder g = new GsonBuilder();
        Gson gson = g.create();
        String json = pref.getString("schedule", "");
        Log.d("json", json);
        Type type = new TypeToken<Map<Long, SchedulePair>>() {
        }.getType();
        hold = (Map<Long, SchedulePair>) gson.fromJson(json, type);
        schedule = new Schedule(hold);


        ListView patList = (ListView) findViewById(R.id.taskList2);
        listAdapter1 = new ListAdapter(getApplicationContext(), schedule, 0);
        patList.setAdapter(listAdapter1);
    }

    @Override
    public void onResume() {
        SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
        GsonBuilder g = new GsonBuilder();
        Gson gson = g.create();
        String json = pref.getString("schedule", "");
        Log.d("json", json);
        Type type = new TypeToken<Map<Long, SchedulePair>>() {
        }.getType();
        hold = (Map<Long, SchedulePair>) gson.fromJson(json, type);
        schedule = new Schedule(hold);
        super.onResume();
        listAdapter1.notifyDataSetChanged();
        Log.d("list", "Updating PatientActivity");
    }


}
