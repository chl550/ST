package com.example.coryliang.scheduletracker;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Date;

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
        if (version == 0) {
            view = layout.inflate(R.layout.activity_list, null);
        }
        else {
            view = layout.inflate(R.layout.activity_list2, null);
        }
        final View tempView = view;
        tempView.setBackgroundColor(Color.RED);
        check = false;
        TextView date = (TextView) view.findViewById(R.id.timeList);
        TextView location = (TextView) view.findViewById(R.id.Location);
        TextView task = (TextView) view.findViewById(R.id.Task);
        Button done = (Button) view.findViewById(R.id.done);
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

                }
            });
        }
        else {
            if (schedule.schedule.get(schedule.getNKey(i)).getStatus() == false) {
                tempView.setBackgroundColor(Color.RED);
                Log.d("list", "updated red2");
            } else {
                tempView.setBackgroundColor(Color.GREEN);
                Log.d("list", "updated green2");
            }
        }

        return view;
    }
}
