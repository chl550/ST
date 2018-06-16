package com.example.coryliang.scheduletracker;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Cory Liang on 6/6/2018.
 */

public class ListAdapter extends BaseAdapter {
    Context context;
    Schedule schedule;
    LayoutInflater layout;
    boolean check;
    int version = 0;
    CaregiverActivity activity = null;
    Map<Long, SchedulePair> hold = null;
    String json = null;

    public ListAdapter(Context context, Schedule schedule, int version) {
        this.context = context;
        this.schedule = schedule;
        layout = (LayoutInflater.from(context));
        this.version = version;

    }

    public ListAdapter(Context context, Schedule schedule, int version, CaregiverActivity activity) {
        this.context = context;
        this.schedule = schedule;
        layout = (LayoutInflater.from(context));
        this.version = version;
        this.activity = activity;
    }


    @Override
    public int getCount() {
        return schedule.getCount();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final SharedPreferences pref = context.getSharedPreferences("data", MODE_PRIVATE);
        final GsonBuilder g = new GsonBuilder();
        final Gson gson = g.create();
        json = pref.getString("schedule", "");
        Type type = new TypeToken<Map<Long, SchedulePair>>() {
        }.getType();
        hold = (Map<Long, SchedulePair>) gson.fromJson(json, type);
        schedule = new Schedule(hold);
        if (version == 0) {
            view = layout.inflate(R.layout.activity_list, null);
        } else if (version == 1){
            view = layout.inflate(R.layout.activity_list2, null);
        }
        final View tempView = view;

        check = false;
        TextView date = (TextView) view.findViewById(R.id.timeList);
        TextView location = (TextView) view.findViewById(R.id.Location);
        TextView task = (TextView) view.findViewById(R.id.Task);
        Button done = (Button) view.findViewById(R.id.done);
        if (schedule.checkTask(schedule.getNKey(i)) == false) {
            tempView.setBackgroundColor(Color.RED);
            Log.d("list", "updated red2");
        } else {
            tempView.setBackgroundColor(Color.GREEN);
            Log.d("list", "updated green2");

        }
        final Date currDate = new Date(schedule.getNKey(i));
        String dateText = schedule.dateToString(currDate);
        date.setText(dateText);
        location.setText(schedule.getNLocation(i));
        task.setText(schedule.getNTask(i));
        if (version == 0) {
            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    schedule.taskDone(schedule.getNKey(i));
                    view.setSelected(true);
                    if (schedule.schedule.get(schedule.getNKey(i)).getStatus() == false) {
                        tempView.setBackgroundColor(Color.RED);
                        Log.d("list", "updated red1");
                    } else {
                        tempView.setBackgroundColor(Color.GREEN);
                        Log.d("list", "updated green1");
                    }
                    SharedPreferences.Editor prefEdit = pref.edit();
                    json = gson.toJson(schedule.getMap());

                    Log.d("json", json);
                    prefEdit.putString("schedule", json);
                    prefEdit.commit();

                }
            });
        }


        return view;
    }
}
