package com.example.coryliang.scheduletracker;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.Expose;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.TreeMap;


/**
 * Created by Cory Liang on 6/2/2018.
 */

public class Schedule implements Parcelable {
    @Expose
    public static Map<Long, SchedulePair> schedule;
    @Expose
    public SchedulePair pair;

    public Schedule() {
        schedule = new TreeMap<Long, SchedulePair>();
    }

    public Schedule(Map<Long, SchedulePair> schedule) {
        this.schedule = schedule;
    }

    public void addTask(Long time, String location, String task) {
        pair = new SchedulePair(location, task, false);
        schedule.put(time, pair);
    }

    //might change later, will only be able to delete a task assuming person has unique times
    public void deleteTask(Long time, String location, String task) {
        schedule.remove(time);

    }

    public void modifyTask(Long time, String location, String task) {
        schedule.get(time).setLocation(location);
        schedule.get(time).setTask(task);
    }

    public void taskDone(Long time) {
        if (schedule.get(time).getStatus() == false) {
            schedule.get(time).setStatus(true);
        } else {
            schedule.get(time).setStatus(false);
        }
    }

    public boolean checkTask(Long time) {
        Log.d("list", "Status was " + schedule.get(time).getStatus());
        return schedule.get(time).getStatus();
    }

    public SchedulePair findTask(Long time) {
        return schedule.get(time);
    }

    public int getCount() {
        return schedule.size();
    }

    public Long getNextFalse() {
        for (Map.Entry<Long, SchedulePair> entry : schedule.entrySet()) {
            if (entry.getValue().getStatus() == false) {
                return entry.getKey();
            }

        }
        long ret = 0;
        return ret;
    }

    public Long getNKey(int i) {
        int count = 0;
        for (Map.Entry<Long, SchedulePair> entry : schedule.entrySet()) {
            if (count == i) {
                return entry.getKey();
            }
            count++;

        }
        return null;
    }

    public String dateToString(Date time) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String ret = df.format(time);
        return ret;
    }

    public String getNLocation(int i) {
        int count = 0;
        for (Map.Entry<Long, SchedulePair> entry : schedule.entrySet()) {
            if (count == i) {
                return entry.getValue().getLocation();
            }
            count++;
        }
        return null;
    }

    public String getNTask(int i) {
        int count = 0;
        for (Map.Entry<Long, SchedulePair> entry : schedule.entrySet()) {
            if (count == i) {
                return entry.getValue().getTask();
            }
            count++;
        }
        return null;
    }

    //only removed if all tasks are finished
    public void removeAll() {
        for (Map.Entry<Long, SchedulePair> entry : schedule.entrySet()) {
            if (entry.getValue().getStatus() == false) {
                Log.d("done", "All tasks not finished yet");
                return;
            }
        }
        schedule.clear();
    }

    public Map getMap() {
        return this.schedule;
    }

    public boolean checkAllTrue() {
        for (Map.Entry<Long, SchedulePair> entry : schedule.entrySet()) {
            if (entry.getValue().getStatus() == false) {
                Log.d("done", "All tasks not finished yet");
                return false;
            }
        }
        return true;
    }

    protected Schedule(Parcel in) {
        pair = (SchedulePair) in.readValue(SchedulePair.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(pair);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Schedule> CREATOR = new Parcelable.Creator<Schedule>() {
        @Override
        public Schedule createFromParcel(Parcel in) {
            return new Schedule(in);
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };
}