package com.example.coryliang.scheduletracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PatientActivity extends AppCompatActivity {
    Schedule schedule = null;

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
        //Bundle getData = getIntent().getExtras();
        //don't know if we need to, keep going until we receive actual info.
        /*
        while(getIntent().getExtras() == null) {
            getData = getIntent().getExtras();
        }*/

       // schedule = (Schedule) getData.getParcelable("schedule");

        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        GsonBuilder g = new GsonBuilder();
        Gson gson = g.create();
        String json = pref.getString("schedule","");
        schedule = gson.fromJson(json, Schedule.class);


        ListView patList = (ListView) findViewById(R.id.taskList2);
        ListAdapter listAdapter1 = new ListAdapter(getApplicationContext(), schedule);
        patList.setAdapter(listAdapter1);
    }



}
