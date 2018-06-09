package com.example.coryliang.scheduletracker;

import android.content.Context;
import android.graphics.Color;
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

    public ListAdapter(Context context, Schedule schedule) {
        this.context = context;
        this.schedule = schedule;
        layout = (LayoutInflater.from(context));
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
        view = layout.inflate(R.layout.activity_list,null);
        final View tempView = view;
        view.setBackgroundColor(Color.RED);
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
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                schedule.taskDone(schedule.getNKey(i));
                view.setSelected(true);
                if (check == false) {
                    check = true;
                }
                tempView.setBackgroundColor(Color.GREEN);

            }
        });

        return view;
    }
}
